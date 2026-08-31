package com.stingers.alttpr.model.api

import com.stingers.alttpr.model.CustomPrizeToggles
import com.stingers.alttpr.model.CustomRegionToggles
import com.stingers.alttpr.model.GameModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CustomizerSeedRequestTest {

    private val json = Json { encodeDefaults = false }

    @Test
    fun `maps every customizer-only field to the expected wire shape`() {
        val model = GameModel(
            tournament = false,
            startingEquipment = setOf(Item.PegasusBoots),
            itemPlacements = mapOf(ItemLocation.Uncle to Item.Bow),
            prizePlacements = mapOf(PrizeLocation.EasternPalace to Prize.GreenPendant),
            medallionPlacements = mapOf(MedallionLocation.TurtleRock to Medallion.Bombos),
            bottlePlacements = mapOf(BottleLocation.WaterfallFairy to BottleApi.BottleRedPotion),
            dropOverrides = mapOf(PrizePack.Pack0 to mapOf(0 to Drop.Heart)),
            textOverrides = mapOf(TextDialog.UncleLeaving to "Hello!"),
            itemCounts = mapOf(Item.TriforcePiece to 30),
            goalRequired = 20,
            prizeToggles = CustomPrizeToggles(shufflePendants = true),
            regionToggles = CustomRegionToggles(wildCompasses = true),
            genericKeys = true,
            dungeonCount = CompassMode.Pickup,
            timerMode = ClockMode.Stopwatch,
            bootsLocationSpoiler = true,
        )

        val request = CustomizerSeedRequest.getRequest(model)
        val encoded = json.encodeToJsonElement(request)

        assertEquals(false, request.tournament)
        assertEquals(listOf("PegasusBoots"), request.eq)

        assertEquals("Bow:1", request.l?.get(ItemLocation.Uncle.value))
        assertEquals("PendantOfCourage:1", request.l?.get(PrizeLocation.EasternPalace.value))
        assertEquals("Bombos:1", request.l?.get(MedallionLocation.TurtleRock.value))
        assertEquals("BottleWithRedPotion:1", request.l?.get(BottleLocation.WaterfallFairy.value))

        assertEquals("Heart", request.drops?.get("0")?.get("0"))
        assertEquals("Hello!", request.texts?.get(TextDialog.UncleLeaving.value))

        assertEquals(30, request.custom?.item?.count?.get("TriforcePiece"))
        assertEquals(20, request.custom?.item?.goal?.required)
        assertEquals(true, request.custom?.prize?.shufflePendants)
        assertEquals(true, request.custom?.region?.wildCompasses)
        assertEquals(true, request.custom?.rom?.genericKeys)
        assertEquals("pickup", request.custom?.rom?.dungeonCount)
        assertEquals("stopwatch", request.custom?.rom?.timerMode)
        assertEquals(true, request.custom?.spoil?.bootsLocation)

        // spot-check the actual encoded JSON tree, not just the intermediate data class
        val root = encoded.jsonObject
        assertEquals("Bow:1", root["l"]!!.jsonObject[ItemLocation.Uncle.value]!!.jsonPrimitive.content)
        assertEquals(
            30,
            root["custom"]!!.jsonObject["item"]!!.jsonObject["count"]!!.jsonObject["TriforcePiece"]!!.jsonPrimitive.int
        )
        assertEquals(
            "pickup",
            root["custom"]!!.jsonObject["rom"]!!.jsonObject["dungeonCount"]!!.jsonPrimitive.content
        )
    }

    @Test
    fun `omits eq, drops, and texts when nothing is set`() {
        val request = CustomizerSeedRequest.getRequest(GameModel())

        assertEquals(false, request.tournament)
        assertNull(request.eq)
        assertNull(request.drops)
        assertNull(request.texts)
    }

    @Test
    fun `always sends l, even empty, since ignoreCanKillEscapeThings reads it unguarded`() {
        val request = CustomizerSeedRequest.getRequest(GameModel())

        assertNotNull(request.l)
        assertEquals(emptyMap(), request.l)

        val root = json.encodeToJsonElement(request).jsonObject
        assertNotNull(root["l"])
        assertTrue(root["l"]!!.jsonObject.isEmpty())
    }

    @Test
    fun `always sends custom, plus the specific leaf fields the backend reads unguarded`() {
        val request = CustomizerSeedRequest.getRequest(GameModel())

        // custom.prize / custom.region / custom.spoil are genuinely optional server-side
        assertNotNull(request.custom)
        assertNull(request.custom?.prize)
        assertNull(request.custom?.region)
        assertNull(request.custom?.spoil)

        // custom.item.require.Lamp and custom.rom.freeItemMenu/freeItemText are read with
        // no `??` fallback in CustomizerController::prepSeed and 409 ("Undefined array key")
        // if absent, so item/rom themselves — and these specific leaves — must always be sent.
        assertEquals(false, request.custom?.item?.require?.lamp)
        assertNull(request.custom?.item?.count)
        assertNull(request.custom?.item?.goal)
        assertEquals(false, request.custom?.rom?.freeItemMenu)
        assertEquals(false, request.custom?.rom?.freeItemText)
        assertNull(request.custom?.rom?.genericKeys)

        val root = json.encodeToJsonElement(request).jsonObject
        val customJson = root["custom"]!!.jsonObject
        assertEquals(false, customJson["item"]!!.jsonObject["require"]!!.jsonObject["Lamp"]!!.jsonPrimitive.boolean)
        assertEquals(false, customJson["rom"]!!.jsonObject["freeItemMenu"]!!.jsonPrimitive.boolean)
        assertEquals(false, customJson["rom"]!!.jsonObject["freeItemText"]!!.jsonPrimitive.boolean)
        assertNull(customJson["prize"])
        assertNull(customJson["region"])
        assertNull(customJson["spoil"])
    }
}
