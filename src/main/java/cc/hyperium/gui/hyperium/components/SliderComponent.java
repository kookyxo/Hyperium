package cc.hyperium.gui.hyperium.components;

import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by Sk1er on today (It will be right for a little bit)
 */
public class SliderComponent extends AbstractTabComponent {
    private final String label;
    private List<String> lines = new ArrayList<>();
    private Field field;
    private Object parentObj;
    private float minVal;
    private float maxVal;
    private boolean isInteger;
    private boolean round;

    private double currentValue;
    private SliderComponent sliderComponent;
    private int width;
    private int x;
    private int y;

    public SliderComponent(AbstractTab tab, List<String> tags, String label, Field field, Object parentObj, float minVal, float maxVal, boolean isInteger, boolean round) {
        super(tab, tags);
        this.label = label;
        this.field = field;
        this.parentObj = parentObj;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.isInteger = isInteger;
        this.round = round;
    }

    private double getDouble() {
        try {
            return field.getDouble(parentObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return minVal;
    }

    public void setDouble(double val) {
        try {
            field.setDouble(parentObj, val);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int getInt() {
        try {
            return field.getInt(parentObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (int) maxVal;
    }

    public void setInt(int val) {
        try {
            field.setInt(parentObj, val);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(int x, int y, int width, int mouseX, int mouseY) {
        HyperiumFontRenderer font = tab.gui.getFont();
        this.x = x;
        this.y = y;
        lines.clear();
        lines = font.splitString(label, (width) / 4); //16 for icon, 3 for render offset and then some more

        GlStateManager.pushMatrix();
        if (hover)
            Gui.drawRect(x, y, x + width, y + 18 * lines.size(), 0xa0000000);
        GlStateManager.popMatrix();

        int line1 = 0;
        for (String line : lines) {
            font.drawString(line.replaceAll("_"," ").toUpperCase(), x + 3, y + 5 + 17 * line1, 0xffffff);
            line1++;
        }
        /*
          val left = (overlayX + w - 105).toFloat()

        val fr = HyperiumMainGui.INSTANCE.fr
        var s = value.toString()
        if (round)
            s = Math.round(value).toString()
        val toFloat = (overlayY + h / 2).toFloat()

        var color = 0xFFFFFFFF.toInt();

        if(!super.enabled){
            color = Color(169, 169, 169).rgb
        }
        fr.drawString(s, left - 5 - fr.getWidth(s), toFloat - 5, color)
        val rightSide = (overlayX + w - 5).toFloat()
        RenderUtils.drawLine(left, toFloat, rightSide, (overlayY + h / 2).toFloat(), 2f, color)
        var d = (value - minVal) / (maxVal - minVal) * 100
        var toInt = (left + d).toInt()
        RenderUtils.drawFilledCircle(toInt, overlayY + h / 2, 5f, color)
         */
        int left = x + width / 2;
        String s = Double.toString(round ? Math.round(currentValue) : currentValue);

        int rightSide = x + width - 5;

        RenderUtils.drawLine(left, y + 9, rightSide, y + 9, 2f, Color.WHITE.getRGB());
        font.drawString(s, left - 8 - font.getWidth(s), y + 4, Color.WHITE.getRGB());
        double d = (currentValue - minVal) / (maxVal - minVal) * width / 2;
        int toInt = (int) (left + d);

        this.width = width;
        RenderUtils.drawFilledCircle(toInt, y + 9, 5f, Color.WHITE.getRGB());
    }

    @Override
    public int getHeight() {
        return 18 * lines.size();
    }


    @Override
    public void onClick(int x, int y) {
        //we don't care about clicks. Its all about those drags
    }

    @Override
    public void mouseEvent(int mouseX, int mouseY) {
        System.out.println(mouseX);
        if (Mouse.isButtonDown(0)) {
            int left = width / 2;
            int rightSide = width - 5;

            mouseX-=left;
            rightSide-=left;
            double percent = (double) mouseX / (double) rightSide;
            if (percent < 0)
                percent = 0;
            if (percent > 1.0)
                percent = 1.0;
            this.currentValue = percent * maxVal;
        }

    }
}
