package frc.robot.commands;

import frc.robot.generated.LimelightHelpers;

public class LHT {

    public void executeVisionCheck() {
    
    boolean targetExist = LimelightHelpers.getTV("limelight");
    double xOffset = LimelightHelpers.getTX("limelight");
    double yOffset = LimelightHelpers.getTY("limelight");


    if (targetExist) {
        System.out.println("target x locked at " + xOffset + " degrees");
        System.out.println("target y locked at " + yOffset + " degrees");
    } else {

        System.out.println("target not locked");
    }}

    public void setPiplineperAlliance(boolean isRedAlliance) {
       if(isRedAlliance) {
        LimelightHelpers.setPipelineIndex("limelight", 2);
       } else {
        LimelightHelpers.setPipelineIndex("limelight", 3);
       }
    }
}



