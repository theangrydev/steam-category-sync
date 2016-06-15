package testinfrastructure;

import io.github.theangrydev.yatspecfluent.Given;

import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class GivenTagsOnSteamForApp implements Given {

    private final TestInfrastructure testInfrastructure;

    private String appId;
    private String[] tags;

    public GivenTagsOnSteamForApp(TestInfrastructure testInfrastructure) {
        this.testInfrastructure = testInfrastructure;
    }

    public GivenTagsOnSteamForApp forAppWithId(String appId) {
        this.appId = appId;
        return this;
    }

    public GivenTagsOnSteamForApp hasPopularTags(String... tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public void prime() {
        testInfrastructure.givenThat(get(urlPathMatching(format("/app/%s", appId)))
                .willReturn(aResponse().withStatus(200).withBody(storePageWithTags())));
    }

    private String storePageWithTags() {
        String tagElements = Arrays.stream(tags).map(tag -> APP_TAG_TEMPLATE.replace("${tag}", tag)).collect(joining());
        return PAGE_TEMPLATE.replace("${tags}", tagElements);
    }

    private static final String APP_TAG_TEMPLATE = "\t\t\t\t\t\t\t\t\t\t\t<a href=\"http://store.steampowered.com/tag/en/RPG/?snr=1_5_9__409\" class=\"app_tag\" style=\"display: none;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t${tag}\t\t\t\t\t\t\t\t\t\t\t\t</a>";

    private static final String PAGE_TEMPLATE = "<html class=\" responsive\">\n" +
            "\t<body class=\"v6 app game_bg responsive_page\">\n" +
            "\t\t<div class=\"responsive_page_frame with_header\">\n" +
            "\t\t\t<div class=\"responsive_page_content\">\n" +
            "\t\t\t\t<div class=\"responsive_page_template_content\">\n" +
            "\t\t\t\t\t<div class=\"game_page_background game\" style=\"background-image: url('http://cdn.akamai.steamstatic.com/steam/apps/2600/page_bg_generated_v6b.jpg?t=1447350961');\">\n" +
            "\t\t\t\t\t\t<div class=\"page_content_ctn\" itemscope=\"\" itemtype=\"http://schema.org/Product\">\n" +
            "\t\t\t\t\t\t\t<div class=\"block\">\n" +
            "\t\t\t\t\t\t\t\t<script type=\"text/javascript\">\n" +
            "\t\t\t\t\t\t\t\t\tvar strRequiredVersion = \"9\";\n" +
            "\t\t\t\t\t\t\t\t\tif ( typeof( g_bIsOnMac ) != 'undefined' && g_bIsOnMac )\n" +
            "\t\t\t\t\t\t\t\t\t\tstrRequiredVersion = \"10.1.0\";\n" +
            "\t\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\tvar bShouldUseHTML5 = BCanPlayWebm() || BDoesUserPreferHTML5() || !swfobject.hasFlashPlayerVersion(strRequiredVersion);\n" +
            "\t\t\t\t\t\t\t\t</script>\n" +
            "\t\t\t\t\t\t\t\t<div class=\"game_background_glow\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"block_content page_content\" id=\"game_highlights\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"rightcol\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<div class=\"glance_ctn\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"glance_ctn_responsive_right\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<!-- when the javascript runs, it will set these visible or not depending on what fits in the area -->\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"glance_tags_ctn popular_tags_ctn\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"glance_tags_label\">Popular user-defined tags for this product:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"glance_tags popular_tags\" data-appid=\"2600\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" +
            "${tags}" + "\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"app_tag add_button\" onclick=\"ShowAppTagModal( 2600 )\">+</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"clear: both;\"></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t<div style=\"clear: both;\"></div>\n" +
            "\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t</div>\n" +
            "\t\t</div>\n" +
            "\t</body>\n" +
            "</html>\n";
}
