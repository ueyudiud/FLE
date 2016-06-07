/**
 * Far core kinetic energy net.<br>
 * This net provide kinetic energy structure.<br>
 * 
 * This net use main net to handle update of tile, 
 * you can subscribe updating by kinetic energy tile,
 * it is suggested for kinetic energy generator,
 * others part (Such as wire, clocking box) needn't 
 * subscribe it.<br>
 * 
 * The local net implemented 
 * {@link farcore.interfaces.energy.kinetic.IKineticAccess},
 * which only have a method to help tile send energy
 * to near by, it should be active when energy net
 * act this tile during receiving energy or updating.<br>
 * 
 * The net update order:<br>
 * 1.Switching cached tile in updating list and update them.<br>
 * 2.Switching selected tile (Used method sendEnergyTo() called),
 * and let them get energy from updated tile, if two tile is
 * trying to send energy each other, they will get stuck together.
 * Clean updated list again if list of "added for sending" is empty,
 * goto step 3, or do this step again.<br>
 * 3.Put tile added during updating into energy net.<br> 
 */
package farcore.energy.kinetic;