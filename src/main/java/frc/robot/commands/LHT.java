package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.generated.LimelightHelpers;

public class LHT {
    public void eVC() {
        boolean targetVisible = LimelightHelpers.getTV("limelight");
        double tx = LimelightHelpers.getTX("limelight");
        double ty = LimelightHelpers.getTY("limelight");

        if (targetVisible) {
            SmartDashboard.putBoolean("Target Found!", true);
            SmartDashboard.putNumber("Degrees offset: ", tx);
            SmartDashboard.putNumber("Vertical offset: ", ty);
        } else {
            SmartDashboard.putBoolean("Searching for target...", false);
        }
    }
}
