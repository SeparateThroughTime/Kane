package kane.physics.contacts;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.ContactManagementInterface;
import kane.genericGame.PassiveAttributes;
import kane.physics.Shape;
import kane.physics.ShapePair;

public class ContactListener {
	
	public static ContactListener CONTACT_LISTENER;
	
	private ContactManagementInterface penetrationInterface;

	private ContactListener(ContactManagementInterface penetrationInterface) {
		this.penetrationInterface = penetrationInterface;
	}
	
	public static void initializateContactListener(ContactManagementInterface penetrationInterface) {
		if (CONTACT_LISTENER == null) {
			CONTACT_LISTENER = new ContactListener(penetrationInterface);
		}
	}
	
	public void penetrated(ShapePair pair) {
		for (int i = 0; i < 2; i++) {
			Shape activeShape;
			Shape passiveShape;
			if (i == 0) {
				activeShape = pair.shapeA;
				passiveShape = pair.shapeB;
			} else {
				activeShape = pair.shapeB;
				passiveShape = pair.shapeA;
			}
			for (int j = 0; j < activeShape.numActiveAttributes; j++) {
				ActiveAttributes activeA = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.numPassiveAttributes; k++) {
					PassiveAttributes passiveA = passiveShape.getPassiveAttribute(k);
					if (activeA == ActiveAttributes.CAMERA_RIGHT && passiveA == PassiveAttributes.PLAYER_ALL) {
						penetrationInterface.playerTouchCameraRight(activeShape, passiveShape);
						
					} else if (activeA == ActiveAttributes.CAMERA_LEFT && passiveA == PassiveAttributes.PLAYER_ALL) {
						penetrationInterface.playerTouchCameraLeft(activeShape, passiveShape);
						
					} else if (activeA == ActiveAttributes.CAMERA_MID_X && passiveA == PassiveAttributes.PLAYER_ALL) {
						penetrationInterface.playerTouchCameraMidX(activeShape, passiveShape);
						
					}
					if (activeA == ActiveAttributes.CAMERA_UP && passiveA == PassiveAttributes.PLAYER_ALL) {
						penetrationInterface.playerTouchCameraUp(activeShape, passiveShape);
						
					} else if (activeA == ActiveAttributes.CAMERA_DOWN && passiveA == PassiveAttributes.PLAYER_ALL) {
						penetrationInterface.playerTouchCameraDown(activeShape, passiveShape);
						
					} else if (activeA == ActiveAttributes.CAMERA_MID_Y && passiveA == PassiveAttributes.PLAYER_ALL) {
						penetrationInterface.playerTouchCameraMidY(activeShape, passiveShape);
						
					}
					if (activeA == ActiveAttributes.SWORD && passiveA == PassiveAttributes.PLAYER_ALL) {
						penetrationInterface.playerCollectsSword(activeShape, passiveShape);

					}
					if (activeA == ActiveAttributes.ATTACKING_FIELD && passiveA == PassiveAttributes.MOB_ALL) {
						penetrationInterface.mobAttacksMob(activeShape, passiveShape);
					}
				}

			}
		}
	}
	
	public void penetration(ShapePair pair) {
		for (int i = 0; i < 2; i++) {
			Shape activeShape;
			Shape passiveShape;
			if (i == 0) {
				activeShape = pair.shapeA;
				passiveShape = pair.shapeB;
			} else {
				activeShape = pair.shapeB;
				passiveShape = pair.shapeA;
			}
			for (int j = 0; j < activeShape.numActiveAttributes; j++) {
				ActiveAttributes activeA = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.numPassiveAttributes; k++) {
					PassiveAttributes passiveA = passiveShape.getPassiveAttribute(k);
					if (activeA == ActiveAttributes.MOB_FEETS && passiveA == PassiveAttributes.PHYSICAL) {
						penetrationInterface.mobStandsOnPhysical(activeShape, passiveShape);
					}
					if (activeA == ActiveAttributes.MOB_FEETS && passiveA == PassiveAttributes.MOB_ALL) {
						penetrationInterface.mobJumpsOnMob(activeShape, passiveShape);
					}
				}
			}
		}
	}
	
	public void separation(ShapePair pair) {
		for (int i = 0; i < 2; i++) {
			Shape activeShape;
			Shape passiveShape;
			if (i == 0) {
				activeShape = pair.shapeA;
				passiveShape = pair.shapeB;
			} else {
				activeShape = pair.shapeB;
				passiveShape = pair.shapeA;
			}
			for (int j = 0; j < activeShape.numActiveAttributes; j++) {
				ActiveAttributes activeA = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.numPassiveAttributes; k++) {
					PassiveAttributes passiveA = passiveShape.getPassiveAttribute(k);
					if (activeA == ActiveAttributes.MOB_FEETS && passiveA == PassiveAttributes.PHYSICAL) {
						penetrationInterface.mobFeetLeavePhysical(activeShape, passiveShape);
					}
				}
			}
		}
	}
}
