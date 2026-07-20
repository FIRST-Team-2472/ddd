package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.LimelightHelpers;

class LimelightHelperTracking {
    public void get() {
        boolean seesTarget = LimelightHelpers.getTV("limelight");
        double xoffset = LimelightHelpers.getTX("limelight");
        double yoffset = LimelightHelpers.getTY("limelight");
        
        if (seesTarget) {
            SmartDashboard.putNumber("Target Found! X offset: ", xoffset);
            SmartDashboard.putNumber("Target Found! Y offset: ", yoffset);
        }
    }
}