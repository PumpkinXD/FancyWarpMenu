/*
 * Copyright (c) 2023. TirelessTraveler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.tirelesstraveler.fancywarpmenu.data.skyblockconstants;

import ca.tirelesstraveler.fancywarpmenu.data.skyblockconstants.menu.ItemMatchCondition;
import ca.tirelesstraveler.fancywarpmenu.data.skyblockconstants.menu.Menu;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static ca.tirelesstraveler.fancywarpmenu.resourceloaders.ResourceLoader.gson;

@SuppressWarnings("unused")
public class SkyBlockConstants {
    public static final String WARP_COMMAND_BASE = "/warp";

    /** Map of match conditions used to identify SkyBlock menus */
    private Map<Menu, List<ItemMatchCondition>> menuMatchingMap;

    /** Chat messages sent by the server when a warp attempt succeeds or fails */
    private WarpMessages warpMessages;
    /** Names of the warp command and its aliases */
    private List<WarpCommandVariant> warpCommandVariants;
    /** Chat messages are checked to see if they start with this string in order to see if the player joined SkyBlock */
    private String skyBlockJoinMessage;

    private SkyBlockConstants() {
    }

    public Map<Menu, List<ItemMatchCondition>> getMenuMatchingMap() {
        return menuMatchingMap;
    }

    /**
     * Returns the inventory slot index of the last {@link ItemMatchCondition} for the given {@link Menu}.
     *
     * @param menu the {@code Menu} to get the inventory slot index from
     * @return the inventory slot index of the last {@code ItemMatchCondition} for the given {@code Menu}
     */
    public int getLastMatchConditionInventorySlotIndex(Menu menu) {
        List<ItemMatchCondition> matchConditions = menuMatchingMap.get(menu);

        return matchConditions.get(matchConditions.size() - 1).getInventorySlotIndex();
    }

    public WarpMessages getWarpMessages() {
        return warpMessages;
    }

    public List<WarpCommandVariant> getWarpCommandVariants() {
        return warpCommandVariants;
    }

    public String getSkyBlockJoinMessage() {
        return skyBlockJoinMessage;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    public static void validateSkyBlockConstants(SkyBlockConstants skyBlockConstants) {
        if (skyBlockConstants == null) {
            throw new NullPointerException("SkyBlock constants cannot be null");
        }

        for(Map.Entry<Menu, List<ItemMatchCondition>> menuMatchingMapEntry : skyBlockConstants.menuMatchingMap.entrySet()) {
            List<ItemMatchCondition> matchConditions = getMenuMatchConditions(menuMatchingMapEntry);

            for (ItemMatchCondition matchCondition : matchConditions) {
                matchCondition.validateCondition();
            }
        }

        WarpMessages.validateWarpMessages(skyBlockConstants.getWarpMessages());

        if (skyBlockConstants.warpCommandVariants == null || skyBlockConstants.warpCommandVariants.isEmpty()) {
            throw new NullPointerException("Warp command variant list cannot be empty");
        }

        for (WarpCommandVariant warpCommandVariant : skyBlockConstants.warpCommandVariants) {
            WarpCommandVariant.validateWarpCommandVariant(warpCommandVariant);
        }
    }

    @NotNull
    private static List<ItemMatchCondition> getMenuMatchConditions(Map.Entry<Menu, List<ItemMatchCondition>> menuMatchingMapEntry) {
        List<ItemMatchCondition> matchConditions = menuMatchingMapEntry.getValue();

        if (matchConditions == null) {
            throw new NullPointerException(String.format("Menu %s's menu match conditions list cannot be null",
                    menuMatchingMapEntry.getKey().name()));
        } else if (matchConditions.isEmpty()) {
            throw new IllegalArgumentException(String.format("Menu %s's menu match conditions list cannot be empty",
                    menuMatchingMapEntry.getKey().name()));
        }
        return matchConditions;
    }
}
