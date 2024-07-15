package kane.physics.contacts;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.ContactManagementInterface;
import kane.genericGame.PassiveAttributes;
import kane.physics.Shape;
import kane.physics.ShapePair;

public class ContactListener{

    public static ContactListener CONTACT_LISTENER;

    private final ContactManagementInterface contactManagementInterface;

    private ContactListener(ContactManagementInterface contactManagementInterface){
        this.contactManagementInterface = contactManagementInterface;
    }

    public static void initializeContactListener(ContactManagementInterface contactManagementInterface){
        if (CONTACT_LISTENER == null){
            CONTACT_LISTENER = new ContactListener(contactManagementInterface);
        }
    }

    public void penetrated(ShapePair pair){
        for (int i = 0; i < 2; i++){
            Shape activeShape;
            Shape passiveShape;
            if (i == 0){
                activeShape = pair.shapeA;
                passiveShape = pair.shapeB;
            } else{
                activeShape = pair.shapeB;
                passiveShape = pair.shapeA;
            }

            for (int j = 0; j < activeShape.numActiveAttributes; j++){
                ActiveAttributes activeA = activeShape.getActiveAttribute(j);
                for (int k = 0; k < passiveShape.numPassiveAttributes; k++){
                    PassiveAttributes passiveA = passiveShape.getPassiveAttribute(k);
                    penetratedCameraXMovement(activeA, passiveA, activeShape, passiveShape);
                    penetratedCameraYMovement(activeA, passiveA, activeShape, passiveShape);
                    penetratedCollectItem(activeA, passiveA, activeShape, passiveShape);
                    penetratedDoDamage(activeA, passiveA, activeShape, passiveShape);
                }

            }
        }
    }

    public void penetration(ShapePair pair){
        for (int i = 0; i < 2; i++){
            Shape activeShape;
            Shape passiveShape;
            if (i == 0){
                activeShape = pair.shapeA;
                passiveShape = pair.shapeB;
            } else{
                activeShape = pair.shapeB;
                passiveShape = pair.shapeA;
            }

            for (int j = 0; j < activeShape.numActiveAttributes; j++){
                ActiveAttributes activeA = activeShape.getActiveAttribute(j);
                for (int k = 0; k < passiveShape.numPassiveAttributes; k++){
                    PassiveAttributes passiveA = passiveShape.getPassiveAttribute(k);
                    penetrationFeetDetection(activeA, passiveA, activeShape, passiveShape);
                }
            }
        }
    }

    public void separation(ShapePair pair){
        for (int i = 0; i < 2; i++){
            Shape activeShape;
            Shape passiveShape;
            if (i == 0){
                activeShape = pair.shapeA;
                passiveShape = pair.shapeB;
            } else{
                activeShape = pair.shapeB;
                passiveShape = pair.shapeA;
            }
            for (int j = 0; j < activeShape.numActiveAttributes; j++){
                ActiveAttributes activeA = activeShape.getActiveAttribute(j);
                for (int k = 0; k < passiveShape.numPassiveAttributes; k++){
                    PassiveAttributes passiveA = passiveShape.getPassiveAttribute(k);
                    separationFeetDetection(activeA, passiveA, activeShape, passiveShape);
                }
            }
        }
    }

    private void penetratedDoDamage(ActiveAttributes activeA, PassiveAttributes passiveA, Shape activeShape,
                                    Shape passiveShape){
        if (activeA == ActiveAttributes.ATTACKING_FIELD && passiveA == PassiveAttributes.MOB_ALL){
            contactManagementInterface.mobAttacksMob(activeShape, passiveShape);
        }
    }

    private void penetratedCollectItem(ActiveAttributes activeA, PassiveAttributes passiveA, Shape activeShape,
                                       Shape passiveShape){
        if (activeA == ActiveAttributes.SWORD && passiveA == PassiveAttributes.PLAYER_ALL){
            contactManagementInterface.playerCollectsSword(activeShape, passiveShape);
        }
    }

    private void penetratedCameraYMovement(ActiveAttributes activeA, PassiveAttributes passiveA, Shape activeShape,
                                           Shape passiveShape){
        if (activeA == ActiveAttributes.CAMERA_UP && passiveA == PassiveAttributes.PLAYER_ALL){
            contactManagementInterface.playerTouchCameraUp(activeShape, passiveShape);

        } else if (activeA == ActiveAttributes.CAMERA_DOWN && passiveA == PassiveAttributes.PLAYER_ALL){
            contactManagementInterface.playerTouchCameraDown(activeShape, passiveShape);

        } else if (activeA == ActiveAttributes.CAMERA_MID_Y && passiveA == PassiveAttributes.PLAYER_ALL){
            contactManagementInterface.playerTouchCameraMidY(activeShape, passiveShape);

        }
    }

    private void penetratedCameraXMovement(ActiveAttributes activeA, PassiveAttributes passiveA, Shape activeShape,
                                           Shape passiveShape){
        if (activeA == ActiveAttributes.CAMERA_RIGHT && passiveA == PassiveAttributes.PLAYER_ALL){
            contactManagementInterface.playerTouchCameraRight(activeShape, passiveShape);

        } else if (activeA == ActiveAttributes.CAMERA_LEFT && passiveA == PassiveAttributes.PLAYER_ALL){
            contactManagementInterface.playerTouchCameraLeft(activeShape, passiveShape);

        } else if (activeA == ActiveAttributes.CAMERA_MID_X && passiveA == PassiveAttributes.PLAYER_ALL){
            contactManagementInterface.playerTouchCameraMidX(activeShape, passiveShape);

        }
    }

    private void penetrationFeetDetection(ActiveAttributes activeA, PassiveAttributes passiveA, Shape activeShape,
                                          Shape passiveShape){
        if (activeA == ActiveAttributes.MOB_FEETS && passiveA == PassiveAttributes.PHYSICAL){
            contactManagementInterface.mobStandsOnPhysical(activeShape, passiveShape);
        }

        if (activeA == ActiveAttributes.MOB_FEETS && passiveA == PassiveAttributes.MOB_ALL){
            contactManagementInterface.mobJumpsOnMob(activeShape, passiveShape);
        }
    }

    private void separationFeetDetection(ActiveAttributes activeA, PassiveAttributes passiveA, Shape activeShape,
                                         Shape passiveShape){
        if (activeA == ActiveAttributes.MOB_FEETS && passiveA == PassiveAttributes.PHYSICAL){
            contactManagementInterface.mobFeetLeavePhysical(activeShape, passiveShape);
        }
    }
}
