package frc.robot.commands;

import frc.robot.generated.LimelightHelpers;

public class PipelineByAlliance {
    boolean isRedAlliance;
    public void setPipeline() {
        if (isRedAlliance) {
            LimelightHelpers.setPipelineIndex("limelight", 2);
        } else {
            LimelightHelpers.setPipelineIndex("limelight", 3);
        }
    }
}
