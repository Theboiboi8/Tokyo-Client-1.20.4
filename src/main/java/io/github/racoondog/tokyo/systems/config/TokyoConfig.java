package io.github.racoondog.tokyo.systems.config;

import io.github.racoondog.tokyo.systems.seedresolver.SeedResolver;
import io.github.racoondog.tokyo.utils.settings.OrderedEnumSetting;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.WindowTabScreen;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.utils.misc.NbtUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TokyoConfig extends System<TokyoConfig> {
    public static final TokyoConfig INSTANCE = new TokyoConfig();
    public final Settings settings = new Settings();

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    // General

    public final Setting<List<SeedResolver.ResolutionMethod>> seedResolutionMethods = sgGeneral.add(new OrderedEnumSetting.Builder<SeedResolver.ResolutionMethod>()
        .name("seed-resolution-methods")
        .description("Priority")
        .defaultValue(SeedResolver.ResolutionMethod.CommandSource, SeedResolver.ResolutionMethod.SeedConfig, SeedResolver.ResolutionMethod.SavedSeedConfigs, SeedResolver.ResolutionMethod.OnlineDatabase)
        .build()
    );

    public final Setting<Boolean> eventDebug = sgGeneral.add(new BoolSetting.Builder()
        .name("event-debugging")
        .description("Adds event handlers to the f3 debug pie.")
        .defaultValue(false)
        .build()
    );

    private TokyoConfig() {
        super("tokyo-config");

        MeteorClient.EVENT_BUS.subscribe(this);
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.put("settings", settings.toTag());

        return tag;
    }

    @Override
    public TokyoConfig fromTag(NbtCompound tag) {
        if (tag.contains("settings")) settings.fromTag(tag.getCompound("settings"));

        return this;
    }

    public static class TokyoConfigTab extends Tab {
        public static final TokyoConfigTab INSTANCE = new TokyoConfigTab();

        private TokyoConfigTab() {
            super("Tokyo");
        }

        @Override
        public TabScreen createScreen(GuiTheme theme) {
            return new TokyoConfigScreen(theme);
        }

        @Override
        public boolean isScreen(Screen screen) {
            return screen instanceof TokyoConfigScreen;
        }
    }

    public static class TokyoConfigScreen extends WindowTabScreen {
        private final Settings settings = TokyoConfig.INSTANCE.settings;

        public TokyoConfigScreen(GuiTheme theme) {
            super(theme, TokyoConfigTab.INSTANCE);

            settings.onActivated();
        }

        @Override
        public void initWidgets() {
            add(theme.settings(settings)).expandX();
        }

        @Override
        public void tick() {
            super.tick();

            settings.tick(window, theme);
        }

        @Override
        public boolean toClipboard() {
            return NbtUtils.toClipboard(TokyoConfig.INSTANCE);
        }

        @Override
        public boolean fromClipboard() {
            return NbtUtils.fromClipboard(TokyoConfig.INSTANCE);
        }
    }
}
