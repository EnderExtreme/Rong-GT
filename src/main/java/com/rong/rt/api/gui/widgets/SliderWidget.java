package com.rong.rt.api.gui.widgets;

import com.google.common.base.Preconditions;
import com.rong.rt.api.gui.GuiTextures;
import com.rong.rt.api.gui.resources.TextureArea;
import com.rong.rt.api.utils.function.FloatConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class SliderWidget extends AbstractPositionedRectangleWidget {

    public static final BiFunction<String, Float, String> DEFAULT_TEXT_SUPPLIER = (name, value) -> I18n.format(name, value.intValue());

    private int sliderWidth = 8;
    private TextureArea backgroundArea = GuiTextures.SLIDER_BACKGROUND;
    private TextureArea sliderIcon = GuiTextures.SLIDER_ICON;
    private BiFunction<String, Float, String> textSupplier = DEFAULT_TEXT_SUPPLIER;
    private int textColor = 0xFFFFFF;

    private final float min;
    private final float max;
    private final String name;

    private final FloatConsumer responder;
    private boolean isPositionSent;

    private String displayString;
    private float sliderPosition;
    public boolean isMouseDown;

    public SliderWidget(String name, int xPosition, int yPosition, int width, int height, float min, float max, float currentValue, FloatConsumer responder) {
        super(xPosition, yPosition, width, height);
        Preconditions.checkNotNull(responder, "responder");
        Preconditions.checkNotNull(name, "name");
        this.min = min;
        this.max = max;
        this.name = name;
        this.responder = responder;
        this.sliderPosition = (currentValue - min) / (max - min);
    }

    public SliderWidget setSliderIcon(@Nonnull TextureArea sliderIcon) {
        Preconditions.checkNotNull(sliderIcon, "sliderIcon");
        this.sliderIcon = sliderIcon;
        return this;
    }

    public SliderWidget setBackground(@Nullable TextureArea background) {
        this.backgroundArea = background;
        return this;
    }

    public SliderWidget setSliderWidth(int sliderWidth) {
        this.sliderWidth = sliderWidth;
        return this;
    }

    public SliderWidget setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    @Override
    public void detectAndSendChanges() {
        if (!isPositionSent) {
            writeUpdateInfo(1, buffer -> buffer.writeFloat(sliderPosition));
            this.isPositionSent = true;
        }
    }

    public float getSliderValue() {
        return this.min + (this.max - this.min) * this.sliderPosition;
    }

    protected String getDisplayString() {
        return textSupplier.apply(name, getSliderValue());
    }

    @Override
    public void drawInBackground(int mouseX, int mouseY) {
        if (backgroundArea != null) {
            backgroundArea.draw(xPosition, yPosition, width, height);
        }
        if (displayString == null) {
            this.displayString = getDisplayString();
        }
        sliderIcon.draw(xPosition + (int) (this.sliderPosition * (float) (this.width - 8)), yPosition, sliderWidth, height);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.drawString(displayString,
            xPosition + width / 2 - fontRenderer.getStringWidth(displayString) / 2,
            yPosition + height / 2 - fontRenderer.FONT_HEIGHT / 2, textColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }

    @Override
    public boolean mouseDragged(int mouseX, int mouseY, int button, long timeDragged) {
        if (this.isMouseDown) {
            this.sliderPosition = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);

            if (this.sliderPosition < 0.0F) {
                this.sliderPosition = 0.0F;
            }

            if (this.sliderPosition > 1.0F) {
                this.sliderPosition = 1.0F;
            }

            this.displayString = this.getDisplayString();
            writeClientAction(1, buffer -> buffer.writeFloat(sliderPosition));
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOver(xPosition, yPosition, width, height, mouseX, mouseY)) {
            this.sliderPosition = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);

            if (this.sliderPosition < 0.0F) {
                this.sliderPosition = 0.0F;
            }

            if (this.sliderPosition > 1.0F) {
                this.sliderPosition = 1.0F;
            }
            this.displayString = this.getDisplayString();
            this.isMouseDown = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int button) {
        this.isMouseDown = false;
        return false;
    }

    @Override
    public void handleClientAction(int id, PacketBuffer buffer) {
        if (id == 1) {
            this.sliderPosition = buffer.readFloat();
            this.sliderPosition = MathHelper.clamp(sliderPosition, 0.0f, 1.0f);
            this.responder.apply(getSliderValue());
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketBuffer buffer) {
        if (id == 1) {
            this.sliderPosition = buffer.readFloat();
            this.sliderPosition = MathHelper.clamp(sliderPosition, 0.0f, 1.0f);
            this.displayString = getDisplayString();
        }
    }
}
