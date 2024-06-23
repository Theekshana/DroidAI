package lnbti.gtp01.droidai.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.util.Log
import lnbti.gtp01.droidai.constants.PreferenceKeys
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.models.CalendarEventObject
import lnbti.gtp01.droidai.models.ErrorBody
import lnbti.gtp01.droidai.models.User
import lnbti.gtp01.droidai.utils.Utils.Companion.getEvents
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

@RunWith(MockitoJUnitRunner::class)
class UtilsTest {

    val siDealersJson = "[\n" +
            "  {\n" +
            "    \"name\": \"ආසිරි ස්ටෝර්ස්\",\n" +
            "    \"phone\": \"762748945\",\n" +
            "    \"district\": \"කුරුණෑගල\",\n" +
            "    \"address\": \"60140, ගිරිඋල්ල , කුරුණෑගල\",\n" +
            "    \"locationUrl\": \"https://www.google.com/maps/place/ASIRI+STORES/@7.3285533,80.118045,16z/data=!4m6!3m5!1s0x3ae31fcd4951ea45:0xa1404f0786a2ff89!8m2!3d7.3285531!4d80.1215593!16s%2Fg%2F11s5jd7jwh?entry=tts\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"ලයන්ස් මැනේජ්මන්ට් (PVT) LTD\",\n" +
            "    \"phone\": \"112300705\",\n" +
            "    \"district\": \"කෑගල්ල\",\n" +
            "    \"address\": \"67J6+82J, කැටවළ පාර ,කෑකුඳෙනිය\",\n" +
            "    \"locationUrl\": \"https://www.google.com/maps/place/Lions/@7.2308443,80.26001,17z/data=!3m1!4b1!4m6!3m5!1s0x3ae31b1395786f23:0x1865e71e919e8c4a!8m2!3d7.2308443!4d80.26001!16s%2Fg%2F11pb0sstwt?entry=tts\"\n" +
            "  }]"

    // Given
    val siContentsJson = "[{\n" +
            "   \"title\":\"අන්නාසි\",\n" +
            "   \"image\":\"https://droid-ai-s3-bucket.s3.amazonaws.com/app-icons/pineappe.jpg\",\n" +
            "   \"type\":\"MenuList\",\n" +
            "   \"items\":[\n" +
            "      {\n" +
            "         \"title\":\"කෘෂි යෙදවුම්\",\n" +
            "         \"image\":\"https://droid-ai-s3-bucket.s3.amazonaws.com/app-icons/agri_inputs.png\",\n" +
            "         \"type\":\"MenuList\",\n" +
            "         \"items\":[\n" +
            "            {\n" +
            "               \"title\":\"රෝපණ ද්\u200Dරව්\u200Dය\",\n" +
            "               \"image\":\"https://droid-ai-s3-bucket.s3.amazonaws.com/app-icons/seeds_planting_material.png\",\n" +
            "               \"type\":\"DetailsPDF\",\n" +
            "               \"link\":\"https://droid-ai-s3-bucket.s3.amazonaws.com/contents/si-vege_frut-fruits-agri-inpt-pltn_material_paple.pdf\"\n" +
            "            },\n" +
            "            {\n" +
            "               \"title\":\"ශාක පෝෂක\",\n" +
            "               \"image\":\"https://droid-ai-s3-bucket.s3.amazonaws.com/app-icons/plant_nutrients.png\",\n" +
            "               \"type\":\"Filter\",\n" +
            "               \"items\":[\n" +
            "                  \n" +
            "               ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"title\":\"ශාක ආරක්ෂණය\",\n" +
            "               \"image\":\"https://droid-ai-s3-bucket.s3.amazonaws.com/app-icons/crop_protection.png\",\n" +
            "               \"type\":\"Filter\",\n" +
            "               \"items\":[\n" +
            "                  \n" +
            "               ]\n" +
            "            }\n" +
            "         ]\n" +
            "      },\n" +
            "      {\n" +
            "         \"title\":\"ප්\u200Dරොටෝකෝල හා තොරතුරු\",\n" +
            "         \"image\":\"https://droid-ai-s3-bucket.s3.amazonaws.com/app-icons/protocols_informations.png\",\n" +
            "         \"type\":\"DetailsImage\",\n" +
            "         \"link\":\"https://droid-ai-s3-bucket.s3.amazonaws.com/contents/si-vege_frut-fruits-protocol.jpg\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"title\":\"වගා උපක්\u200Dරම\",\n" +
            "         \"image\":\"https://droid-ai-s3-bucket.s3.amazonaws.com/app-icons/agri_methods.png\",\n" +
            "         \"type\":\"DetailsPDF\",\n" +
            "         \"link\":\"https://droid-ai-s3-bucket.s3.amazonaws.com/contents/si-vege_frut-fruits-farming_tips.pdf\"\n" +
            "      }\n" +
            "   ]\n" +
            "}]"

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var assets: AssetManager

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        // Mock SharedPreferences
        val sharedPreferences = mock<SharedPreferences>()

        // Initialize SharedPreferencesManager with the mocked SharedPreferences
        SharedPreferencesManager.init(sharedPreferences)
        `when`(context.assets).thenReturn(assets)
    }

    @Test
    fun `test getLoggedInUser with valid JSON`() {
        // Given
        val userJson =
            "{\"nic\":\"911240800V\",\"email\":\"charithvin@gmail.com\",\"firstName\":\"Charith\",\"lastName\":\"Vithanage\",\"gender\":\"Male\",\"userRole\":\"Farmer\",\"dob\":\"1991-05-03\",\"contactNo\":\"0712345678\"}"
        val expectedUser =
            User(
                "911240800V", "charithvin@gmail.com", "Charith", "Vithanage",
                "Male", "Farmer", "1991-05-03", "0712345678"
            )

        // Mock SharedPreferencesManager behavior
        `when`(SharedPreferencesManager.getPreference(PreferenceKeys.LOGGED_IN_USER)).thenReturn(
            userJson
        )

        // When
        val loggedInUser = Utils.getLoggedInUser()

        // Then
        assertEquals(expectedUser, loggedInUser)
    }

    @Test
    fun `test getCategoryList`() {
        `when`(SharedPreferencesManager.getSelectedLanguage()).thenReturn(StringConstants.SINHALA)
        `when`(context.assets.open("si_contents.json")).thenReturn(siContentsJson.byteInputStream())

        // When
        val categoryList = Utils.getCategoryList(context)

        // Then
        assertEquals(1, categoryList.size)
        assertEquals("අන්නාසි", categoryList[0].title)
    }

    @Test
    fun `test getDealerList`() {
        // Given
        `when`(SharedPreferencesManager.getSelectedLanguage()).thenReturn(StringConstants.SINHALA)
        `when`(context.assets.open("si_dealers.json")).thenReturn(siDealersJson.byteInputStream())

        // When
        val dealerList = Utils.getDealerList(context)

        // Then
        assertEquals(2, dealerList.size)
        assertEquals("ආසිරි ස්ටෝර්ස්", dealerList[0].name)
    }

    @Test
    fun `test getEvents with valid serialized JSON`() {
        // Given
        val serializedEvents = "[\n" +
                "    {\n" +
                "        \"dateString\": \"2024-05-01\",\n" +
                "        \"eventString\": \"Event 1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"dateString\": \"2022-06-01\",\n" +
                "        \"eventString\": \"Event 2\"\n" +
                "    }\n" +
                "]"
        val expectedEvents = listOf(
            CalendarEventObject("2024-05-01", "Event 1"),
            CalendarEventObject("2022-06-01", "Event 2")
        )

        // Mock SharedPreferencesManager behavior
        `when`(SharedPreferencesManager.getPreference(PreferenceKeys.EVENTS)).thenReturn(
            serializedEvents
        )

        // When
        val events = getEvents()

        // Then
        assertEquals(expectedEvents, events)
    }

    @Test
    fun `test getEvents with empty serialized JSON`() {
        // Given
        val emptyJson = ""

        // Mock SharedPreferencesManager behavior
        `when`(SharedPreferencesManager.getPreference(PreferenceKeys.EVENTS)).thenReturn(emptyJson)

        // When
        val events = getEvents()

        // Then
        assertEquals(emptyList<CalendarEventObject>(), events)
    }

    @Test
    fun `test getEvents with null serialized JSON`() {
        // Given
        val serializedEvents: String? = null

        // Mock SharedPreferencesManager behavior
        `when`(SharedPreferencesManager.getPreference(PreferenceKeys.EVENTS)).thenReturn(
            serializedEvents
        )

        // When
        val events = getEvents()

        // Then
        assertEquals(emptyList<CalendarEventObject>(), events)
    }

    @Test
    fun `test getErrorBodyFromResponse`() {
        // Given
        val errorJson = "{\"message\": \"Not Found\"}"
        val errorBody = mock(ResponseBody::class.java)
        `when`(errorBody.charStream()).thenReturn(InputStreamReader(ByteArrayInputStream(errorJson.toByteArray())))
        // When
        val errorBodyObject = Utils.getErrorBodyFromResponse(errorBody)

        // Then
        val expectedErrorBody = ErrorBody("Not Found")
        assertEquals(expectedErrorBody, errorBodyObject)
    }
}