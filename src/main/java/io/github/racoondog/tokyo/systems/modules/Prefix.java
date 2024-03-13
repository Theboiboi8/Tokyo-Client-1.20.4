package io.github.racoondog.tokyo.systems.modules;

import io.github.racoondog.tokyo.Tokyo;
import io.github.racoondog.tokyo.utils.prefix.FormattedText;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.settings.GenericSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("ConstantConditions")
@Environment(EnvType.CLIENT)
public class Prefix extends Module {
    public static final Prefix INSTANCE = new Prefix();

    public static final Text DEFAULT_METEOR = ChatUtils.getMeteorPrefix();
    public static final Text DEFAULT_TOKYO = Tokyo.getDefaultPrefix();

    private final SettingGroup sgMeteor = this.settings.createGroup("Meteor");
    private final SettingGroup sgTokyo = this.settings.createGroup("Tokyo");

    // Meteor
    private final Setting<FormattedText> meteorPrefixTextSetting = sgMeteor.add(new GenericSetting.Builder<FormattedText>()
        .name("meteor-prefix")
        .defaultValue(new FormattedText("Meteor", MeteorClient.ADDON.color).onChanged(o -> updateMeteor()))
        .build()
    );

    private final Setting<FormattedText> meteorBorderLeftTextSetting = sgMeteor.add(new GenericSetting.Builder<FormattedText>()
        .name("meteor-border-left")
        .defaultValue(new FormattedText("[", new Color(Formatting.GRAY.getColorValue())).onChanged(o -> updateMeteor()))
        .build()
    );

    private final Setting<FormattedText> meteorBorderRightTextSetting = sgMeteor.add(new GenericSetting.Builder<FormattedText>()
        .name("meteor-border-right")
        .defaultValue(new FormattedText("] ", new Color(Formatting.GRAY.getColorValue())).onChanged(o -> updateMeteor()))
        .build()
    );

    // Tokyo
    public final Setting<FormattedText> tokyoPrefixTextSetting = sgTokyo.add(new GenericSetting.Builder<FormattedText>()
        .name("tokyo-prefix")
        .defaultValue(new FormattedText("Tokyo", FormattedText.Mode.Fade, Style.EMPTY.withColor(Tokyo.INSTANCE.color.getPacked()).withBold(true), Tokyo.INSTANCE.color, new Color(112, 100, 129, 255)).onChanged(o -> updateTokyo()))
        .build()
    );

    private final Setting<FormattedText> tokyoBorderLeftTextSetting = sgTokyo.add(new GenericSetting.Builder<FormattedText>()
        .name("tokyo-border-left")
        .defaultValue(new FormattedText("[", new Color(Formatting.GRAY.getColorValue())).onChanged(o -> updateTokyo()))
        .build()
    );

    private final Setting<FormattedText> tokyoBorderRightTextSetting = sgTokyo.add(new GenericSetting.Builder<FormattedText>()
        .name("tokyo-border-right")
        .defaultValue(new FormattedText("] ", new Color(Formatting.GRAY.getColorValue())).onChanged(o -> updateTokyo()))
        .build()
    );
    private Text METEOR;
    private Text TOKYO;
    private Text INNER_TOKYO;

    @ApiStatus.Internal
    public static int indexOffset = 0;


    private Prefix() {
        super(Tokyo.CATEGORY, "prefix", "Modify Meteor and Tokyo's chat prefixes.");
    }

    @Override
    public void onActivate() {
        updateMeteor();
        updateTokyo();
    }

    private void updateMeteor() {
        if (!isActive()) return;

        METEOR = Text.empty().append(meteorBorderLeftTextSetting.get().get()).append(meteorPrefixTextSetting.get().get()).append(meteorBorderRightTextSetting.get().get());
    }

    private void updateTokyo() {
        if (!isActive()) return;

        INNER_TOKYO = tokyoPrefixTextSetting.get().get();
        TOKYO = Text.empty().append(tokyoBorderLeftTextSetting.get().get()).append(INNER_TOKYO).append(tokyoBorderRightTextSetting.get().get());
    }

    public static Text getMeteor() {
        return INSTANCE.isActive() ? INSTANCE.METEOR : DEFAULT_METEOR;
    }

    public static Text getTokyo() {
        return INSTANCE.isActive() && INSTANCE.TOKYO != null ? INSTANCE.TOKYO : DEFAULT_TOKYO;
    }

    public static Text getInnerTokyo() {
        return INSTANCE.isActive() && INSTANCE.INNER_TOKYO != null ? INSTANCE.INNER_TOKYO : Tokyo.getDefaultInnerPrefix();
    }
}
