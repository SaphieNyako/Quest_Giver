package com.feywild.quest_giver.quest;

import com.feywild.quest_giver.util.RenderEnum;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum QuestNumber {

    //TODO make this datadriven...

    QUEST_0000("quest_0000"), QUEST_0001("quest_0001"), QUEST_0002("quest_0002"), QUEST_0003("quest_0003"), QUEST_0004("quest_0004"),
    QUEST_0005("quest_0005"), QUEST_0006("quest_0006"), QUEST_0007("quest_0007"), QUEST_0008("quest_0008"), QUEST_0009("quest_0009"),
    QUEST_0010("quest_0010"), QUEST_0011("quest_0011"), QUEST_0012("quest_0012"), QUEST_0013("quest_0013"), QUEST_0014("quest_0014"),
    QUEST_0015("quest_0015"), QUEST_0016("quest_0016"), QUEST_0017("quest_0017"), QUEST_0018("quest_0018"), QUEST_0019("quest_0019"),
    QUEST_0020("quest_0020"), QUEST_0021("quest_0021"), QUEST_0022("quest_0022"), QUEST_0023("quest_0023"), QUEST_0024("quest_0024"),
    QUEST_0025("quest_0025"), QUEST_0026("quest_0026"), QUEST_0027("quest_0027"), QUEST_0028("quest_0028"), QUEST_0029("quest_0029"),
    QUEST_0030("quest_0030"), QUEST_0031("quest_0031"), QUEST_0032("quest_0032"), QUEST_0033("quest_0033"), QUEST_0034("quest_0034"),
    QUEST_0035("quest_0035"), QUEST_0036("quest_0036"), QUEST_0037("quest_0037"), QUEST_0038("quest_0038"), QUEST_0039("quest_0039"),
    QUEST_0040("quest_0040"), QUEST_0041("quest_0041"), QUEST_0042("quest_0042"), QUEST_0043("quest_0043"), QUEST_0044("quest_0044"),
    QUEST_0045("quest_0045"), QUEST_0046("quest_0046"), QUEST_0047("quest_0047"), QUEST_0048("quest_0048"), QUEST_0049("quest_0049"),
    QUEST_0050("quest_0050"), QUEST_0051("quest_0051"), QUEST_0052("quest_0052"), QUEST_0053("quest_0053"), QUEST_0054("quest_0054"),
    QUEST_0055("quest_0055"), QUEST_0056("quest_0056"), QUEST_0057("quest_0057"), QUEST_0058("quest_0058"), QUEST_0059("quest_0059"),
    QUEST_0060("quest_0060"), QUEST_0061("quest_0061"), QUEST_0062("quest_0062"), QUEST_0063("quest_0063"), QUEST_0064("quest_0064"),
    QUEST_0065("quest_0065"), QUEST_0066("quest_0066"), QUEST_0067("quest_0067"), QUEST_0068("quest_0068"), QUEST_0069("quest_0069"),
    QUEST_0070("quest_0070"), QUEST_0071("quest_0071"), QUEST_0072("quest_0072"), QUEST_0073("quest_0073"), QUEST_0074("quest_0074"),
    QUEST_0075("quest_0075"), QUEST_0076("quest_0076"), QUEST_0077("quest_0077"), QUEST_0078("quest_0078"), QUEST_0079("quest_0079"),
    QUEST_0080("quest_0080"), QUEST_0081("quest_0081"), QUEST_0082("quest_0082"), QUEST_0083("quest_0083"), QUEST_0084("quest_0084"),
    QUEST_0085("quest_0085"), QUEST_0086("quest_0086"), QUEST_0087("quest_0087"), QUEST_0088("quest_0088"), QUEST_0089("quest_0089"),
    QUEST_0090("quest_0090"), QUEST_0091("quest_0091"), QUEST_0092("quest_0092"), QUEST_0093("quest_0093"), QUEST_0094("quest_0094"),
    QUEST_0095("quest_0095"), QUEST_0096("quest_0096"), QUEST_0097("quest_0097"), QUEST_0098("quest_0098"), QUEST_0099("quest_0099"),
    QUEST_0100("quest_0100"), QUEST_0101("quest_0101"), QUEST_0102("quest_0102"), QUEST_0103("quest_0103"), QUEST_0104("quest_0104"),
    QUEST_0105("quest_0105"), QUEST_0106("quest_0106"), QUEST_0107("quest_0107"), QUEST_0108("quest_0108"), QUEST_0109("quest_0109"),
    QUEST_0110("quest_0110"), QUEST_0111("quest_0111"), QUEST_0112("quest_0112")
    ;


    public final String id;

    QuestNumber(String id) {
        this.id = id;

    }

    public static QuestNumber byId(String id) {
        switch (id.toLowerCase(Locale.ROOT).trim()) {
            case "quest_0000": return QUEST_0000;
            case "quest_0001": return QUEST_0001;
            case "quest_0002": return QUEST_0002;
            case "quest_0003": return QUEST_0003;
            case "quest_0004": return QUEST_0004;
            case "quest_0005": return QUEST_0005;
            case "quest_0006": return QUEST_0006;
            case "quest_0007": return QUEST_0007;
            case "quest_0008": return QUEST_0008;
            case "quest_0009": return QUEST_0009;
            case "quest_0010": return QUEST_0010;
            case "quest_0011": return QUEST_0011;
            case "quest_0012": return QUEST_0012;
            case "quest_0013": return QUEST_0013;
            case "quest_0014": return QUEST_0014;
            case "quest_0015": return QUEST_0015;
            case "quest_0016": return QUEST_0016;
            case "quest_0017": return QUEST_0017;
            case "quest_0018": return QUEST_0018;
            case "quest_0019": return QUEST_0019;
            case "quest_0020": return QUEST_0020;
            case "quest_0021": return QUEST_0021;
            case "quest_0022": return QUEST_0022;
            case "quest_0023": return QUEST_0023;
            case "quest_0024": return QUEST_0024;
            case "quest_0025": return QUEST_0025;
            case "quest_0030": return QUEST_0030;
            case "quest_0031": return QUEST_0031;
            case "quest_0032": return QUEST_0032;
            case "quest_0033": return QUEST_0033;
            case "quest_0034": return QUEST_0034;
            case "quest_0035": return QUEST_0035;
            case "quest_0036": return QUEST_0036;
            case "quest_0037": return QUEST_0037;
            case "quest_0038": return QUEST_0038;
            case "quest_0039": return QUEST_0039;
            case "quest_0040": return QUEST_0040;
            case "quest_0041": return QUEST_0041;
            case "quest_0042": return QUEST_0042;
            case "quest_0043": return QUEST_0043;
            case "quest_0044": return QUEST_0044;
            case "quest_0045": return QUEST_0045;
            case "quest_0046": return QUEST_0046;
            case "quest_0047": return QUEST_0047;
            case "quest_0048": return QUEST_0048;
            case "quest_0049": return QUEST_0049;
            case "quest_0050": return QUEST_0050;
            case "quest_0051": return QUEST_0051;
            case "quest_0052": return QUEST_0052;
            case "quest_0053": return QUEST_0053;
            case "quest_0054": return QUEST_0054;
            case "quest_0055": return QUEST_0055;
            case "quest_0056": return QUEST_0056;
            case "quest_0057": return QUEST_0057;
            case "quest_0058": return QUEST_0058;
            case "quest_0059": return QUEST_0059;
            case "quest_0060": return QUEST_0060;
            case "quest_0061": return QUEST_0061;
            case "quest_0062": return QUEST_0062;
            case "quest_0063": return QUEST_0063;
            case "quest_0064": return QUEST_0064;
            case "quest_0065": return QUEST_0065;
            case "quest_0066": return QUEST_0066;
            case "quest_0067": return QUEST_0067;
            case "quest_0068": return QUEST_0068;
            case "quest_0069": return QUEST_0069;
            case "quest_0070": return QUEST_0070;
            case "quest_0071": return QUEST_0071;
            case "quest_0072": return QUEST_0072;
            case "quest_0073": return QUEST_0073;
            case "quest_0074": return QUEST_0074;
            case "quest_0075": return QUEST_0075;
            case "quest_0076": return QUEST_0076;
            case "quest_0077": return QUEST_0077;
            case "quest_0078": return QUEST_0078;
            case "quest_0079": return QUEST_0079;
            case "quest_0080": return QUEST_0080;
            case "quest_0081": return QUEST_0081;
            case "quest_0082": return QUEST_0082;
            case "quest_0083": return QUEST_0083;
            case "quest_0084": return QUEST_0084;
            case "quest_0085": return QUEST_0085;
            case "quest_0086": return QUEST_0086;
            case "quest_0087": return QUEST_0087;
            case "quest_0088": return QUEST_0088;
            case "quest_0089": return QUEST_0089;
            case "quest_0090": return QUEST_0090;
            case "quest_0091": return QUEST_0091;
            case "quest_0092": return QUEST_0092;
            case "quest_0093": return QUEST_0093;
            case "quest_0094": return QUEST_0094;
            case "quest_0095": return QUEST_0095;
            case "quest_0096": return QUEST_0096;
            case "quest_0097": return QUEST_0097;
            case "quest_0098": return QUEST_0098;
            case "quest_0099": return QUEST_0099;
            case "quest_0100": return QUEST_0100;
            case "quest_0101": return QUEST_0101;
            case "quest_0102": return QUEST_0102;
            case "quest_0103": return QUEST_0103;
            case "quest_0104": return QUEST_0104;
            case "quest_0105": return QUEST_0105;
            case "quest_0106": return QUEST_0106;
            case "quest_0107": return QUEST_0107;
            case "quest_0108": return QUEST_0108;
            case "quest_0109": return QUEST_0109;
            case "quest_0110": return QUEST_0110;
            case "quest_0111": return QUEST_0111;
            case "quest_0112": return QUEST_0112;
            default: throw new IllegalArgumentException("Invalid quest number: " + id);
        }

    }

    public static String optionId(@Nullable QuestNumber questNumber) {
        return questNumber == null ? "unassigned" : questNumber.id;
    }

    @Nullable
    public static QuestNumber byOptionId(String id) {
        try {
            return byId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
