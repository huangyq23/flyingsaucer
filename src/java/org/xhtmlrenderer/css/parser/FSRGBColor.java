/*
 * {{{ header & license
 * Copyright (c) 2007 Wisconsin Court System
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * }}}
 */
package org.xhtmlrenderer.css.parser;

import java.awt.Color;

import com.lowagie.text.pdf.CMYKColor;

/**
 * Container for color information.
 *
 * Can contain either RGB or CMYK color, and if it is CMYK, then itext is required.
 */
public class FSRGBColor {
    private boolean isRgb = true;

    private int _red;
    private int _green;
    private int _blue;

    private int _cyan;
    private int _magenta;
    private int _yellow;
    private int _black;
    
    public FSRGBColor(int red, int green, int blue) {
        if (red < 0 || red > 255) {
            throw new IllegalArgumentException();
        }
        if (green < 0 || green > 255) {
            throw new IllegalArgumentException();
        }
        if (blue < 0 || blue > 255) {
            throw new IllegalArgumentException();
        }        
        _red = red;
        _green = green;
        _blue = blue;
    }

    /**
     * Create a CMYK color with the exact values from the CSS, which are
     * integer percentages from 0-100.
     */
    public FSRGBColor(int cyan, int magenta, int yellow, int black) {
        if (cyan < 0 || cyan > 100) {
            throw new IllegalArgumentException();
        }
        if (magenta < 0 || magenta > 100) {
            throw new IllegalArgumentException();
        }
        if (yellow < 0 || yellow > 100) {
            throw new IllegalArgumentException();
        }
        if (black < 0 || black > 100) {
            throw new IllegalArgumentException();
        }
        isRgb = false;
        _cyan = cyan;
        _magenta = magenta;
        _yellow = yellow;
        _black = black;
    }

    public String toString() {
        if (isRgb) {
            return '#' + toString(_red) + toString(_green) + toString(_blue);
        }
        return "cmyk(" + _cyan + "," + _magenta + "," + _yellow + "," + _black + ")";
    }
    
    private String toString(int color) {
        String result = Integer.toHexString(color);
        if (result.length() == 1) {
            return "0" + result;
        } else {
            return result;
        }
    }
    
    public Color toAWTColor() {
        if (isRgb) {
            return new Color(_red, _green, _blue);
        }
        return new CMYKColor(normalize(_cyan),
                normalize(_magenta),
                normalize(_yellow),
                normalize(_black));
    }

    /**
     * Normalizes an int percentage (0-100) to a float (0.0-1.0)
     */
    private float normalize(int percent) {
        return (percent / 100.0f);
    }
}
