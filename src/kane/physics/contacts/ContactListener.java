package kane.physics.contacts;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.Game;
import kane.genericGame.PassiveAttributes;
import kane.genericGame.ContactManagementInterface;
import kane.genericGame.item.SWORD;
import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.renderer.Renderer;

public class ContactListener {
	private ContactManagementInterface penetrationInterface;

	public ContactListener(ContactManagementInterface penetrationInterface) {
		this.penetrationInterface = penetrationInterface;
	}
	
	public void penetrated(ShapePair pair) {
		for (int i = 0; i < 2; i++) {
			Shape activeShape;
			Shape passiveShape;
			if (i == 0) {
				activeShape = pair.getShapeA();
				passiveShape = pair.getShapeB();
			} else {
				activeShape = pair.getShapeB();
				passiveShape = pair.getShapeA();
			}
			for (int j = 0; j < activeShape.getNumActiveAttributes(); j++) {
				ActiveAttributes activeA = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.getNumPassiveAttributes(); k++) {
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
				activeShape = pair.getShapeA();
				passiveShape = pair.getShapeB();
			} else {
				activeShape = pair.getShapeB();
				passiveShape = pair.getShapeA();
			}
			for (int j = 0; j < activeShape.getNumActiveAttributes(); j++) {
				ActiveAttributes activeA = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.getNumPassiveAttributes(); k++) {
					PassiveAttributes passiveA = passiveShape.getPassiveAttribute(k);
					if (activeA == ActiveAttributes.PLAYER_FEETS && passiveA == PassiveAttributes.PHYSICAL) {
						penetrationInterface.mobStandsOnPhysical(activeShape, passiveShape);
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
				activeShape = pair.getShapeA();
				passiveShape = pair.getShapeB();
			} else {
				activeShape = pair.getShapeB();
				passiveShape = pair.getShapeA();
			}
			for (int j = 0; j < activeShape.getNumActiveAttributes(); j++) {
				ActiveAttributes activeA = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.getNumPassiveAttributes(); k++) {
					PassiveAttributes passiveA = passiveShape.getPassiveAttribute(k);
					if (activeA == ActiveAttributes.PLAYER_FEETS && passiveA == PassiveAttributes.PHYSICAL) {
						penetrationInterface.mobFeetLeavePhysical(activeShape, passiveShape);
					}
				}
			}
		}
	}
}
