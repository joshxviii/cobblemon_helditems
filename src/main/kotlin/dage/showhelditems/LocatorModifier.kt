package dage.showhelditems

import dage.showhelditems.HeldItemModifierParser.HeldItemModifier

interface LocatorModifier {
    var itemModifiers: Array<HeldItemModifier>
}