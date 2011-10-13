/*
 * {{{ header & license
 * Copyright (c) 2004, 2005 Joshua Marinacci
 * Copyright (c) 2006, 2007 Wisconsin Court System
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
package org.xhtmlrenderer.render;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.xhtmlrenderer.css.style.CalculatedStyle;
import org.xhtmlrenderer.css.style.CssContext;
import org.xhtmlrenderer.layout.InlineBoxing;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.layout.Styleable;

/**
 * An anonymous block box as defined in the CSS spec.  This class is only used
 * when wrapping inline content in a block box in order to ensure that a block
 * box only ever contains either block or inline content.  Other anonymous block
 * boxes create a <code>BlockBox</code> directly with the anonymous property is
 * true.
 */
public class AnonymousBlockBox extends BlockBox {
    private List _openInlineBoxes;
    
    public AnonymousBlockBox(Element element) {
        setElement(element);
    }

    public void layout(LayoutContext c) {
        InlineBoxing.layoutContent(c, this, 0);
    }

    public int getContentWidth() {
        return getContainingBlock().getContentWidth();
    }
    
    public Box find(CssContext cssCtx, int absX, int absY) {
        Box result = super.find(cssCtx, absX, absY);
        if (result == this) {
            return getParent();
        } else {
            return result;
        }
    }

    public List getOpenInlineBoxes() {
        return _openInlineBoxes;
    }

    public void setOpenInlineBoxes(List openInlineBoxes) {
        _openInlineBoxes = openInlineBoxes;
    }
    
    public boolean isSkipWhenCollapsingMargins() {
        // An anonymous block will already have its children provided to it
        for (Iterator i = getInlineContent().iterator(); i.hasNext(); ) {
            Styleable styleable = (Styleable)i.next();
            CalculatedStyle style = styleable.getStyle();
            if (! (style.isFloated() || style.isAbsolute() || style.isFixed())) {
                return false;
            }
        }
        return true;
    }
    
    public void provideSiblingMarginToFloats(int margin) {
        for (Iterator i = getInlineContent().iterator(); i.hasNext(); ) {
            Styleable styleable = (Styleable)i.next();
            if (styleable instanceof BlockBox) {
                BlockBox b = (BlockBox)styleable;
                if (b.isFloated()) {
                    b.getFloatedBoxData().setMarginFromSibling(margin);
                }
            }
        }
    }
    
    public boolean isMayCollapseMarginsWithChildren() {
        return false;
    }
    
    public void styleText(LayoutContext c) {
        styleText(c, getParent().getStyle());
    }    
}

/*
 * $Id: AnonymousBlockBox.java,v 1.24 2007-06-07 16:56:29 peterbrant Exp $
 *
 * $Log: not supported by cvs2svn $
 * Revision 1.23  2007/05/10 00:39:38  peterbrant
 * Anonymous block boxes were always being skipped when collapsing margins
 *
 * Revision 1.22  2007/03/12 21:11:20  peterbrant
 * Documentation update
 *
 * Revision 1.21  2007/02/07 16:33:24  peterbrant
 * Initial commit of rewritten table support and associated refactorings
 *
 * Revision 1.20  2006/09/08 15:41:57  peterbrant
 * Calculate containing block width accurately when collapsing margins / Provide collapsed bottom
 * margin to floats / Revive :first-line and :first-letter / Minor simplication in InlineBoxing
 * (get rid of now-superfluous InlineBoxInfo)
 *
 * Revision 1.19  2006/09/01 23:49:38  peterbrant
 * Implement basic margin collapsing / Various refactorings in preparation for shrink-to-fit / Add hack to treat auto margins as zero
 *
 * Revision 1.18  2006/08/29 17:29:13  peterbrant
 * Make Style object a thing of the past
 *
 * Revision 1.17  2006/08/27 00:36:39  peterbrant
 * Initial commit of (initial) R7 work
 *
 * Revision 1.16  2006/02/22 02:20:18  peterbrant
 * Links and hover work again
 *
 * Revision 1.15  2005/11/25 16:57:19  peterbrant
 * Initial commit of inline content refactoring
 *
 * Revision 1.14  2005/11/12 21:55:27  tobega
 * Inline enhancements: block box text decorations, correct line-height when it is a number, better first-letter handling
 *
 * Revision 1.13  2005/11/05 03:29:59  peterbrant
 * Start work on painting order and improved positioning implementation
 *
 * Revision 1.12  2005/01/29 20:24:24  pdoubleya
 * Clean/reformat code. Removed commented blocks, checked copyright.
 *
 * Revision 1.11  2005/01/07 00:29:29  tobega
 * Removed Content reference from Box (mainly to reduce memory footprint). In the process stumbled over and cleaned up some messy stuff.
 *
 * Revision 1.10  2005/01/05 01:10:15  tobega
 * Went wild with code analysis tool. removed unused stuff. Lucky we have CVS...
 *
 * Revision 1.9  2004/12/12 03:32:59  tobega
 * Renamed x and u to avoid confusing IDE. But that got cvs in a twist. See if this does it
 *
 * Revision 1.8  2004/12/11 23:36:49  tobega
 * Progressing on cleaning up layout and boxes. Still broken, won't even compile at the moment. Working hard to fix it, though.
 *
 * Revision 1.7  2004/12/10 06:51:04  tobega
 * Shamefully, I must now check in painfully broken code. Good news is that Layout is much nicer, and we also handle :before and :after, and do :first-line better than before. Table stuff must be brought into line, but most needed is to fix Render. IMO Render should work with Boxes and Content. If Render goes for a node, that is wrong.
 *
 * Revision 1.6  2004/12/05 19:42:44  tobega
 * Made recursion in InlineUtil easier to understand
 *
 * Revision 1.5  2004/12/05 18:11:38  tobega
 * Now uses style cache for pseudo-element styles. Also started preparing to replace inline node handling with inline content handling.
 *
 * Revision 1.4  2004/12/05 00:48:58  tobega
 * Cleaned up so that now all property-lookups use the CalculatedStyle. Also added support for relative values of top, left, width, etc.
 *
 * Revision 1.3  2004/10/23 13:50:26  pdoubleya
 * Re-formatted using JavaStyle tool.
 * Cleaned imports to resolve wildcards except for common packages (java.io, java.util, etc).
 * Added CVS log comments at bottom.
 *
 *
 */

