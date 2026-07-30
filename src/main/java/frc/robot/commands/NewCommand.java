package frc.robot.commands;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NewCommand {
    private NetworkTable table;
    
    public NewCommand() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public void readTelemetry() {
        NetworkTableEntry txEntry = table.getEntry("tx");
        NetworkTableEntry tvEntry = table.getEntry("tv");

        double tx = txEntry.getDouble(0.0);
        boolean targetVisible = tvEntry.getDouble(0.0) == 1.0;

        if (targetVisible) {
            SmartDashboard.putNumber("Target Found! Degrees offset: ", tx);
        } else {
            SmartDashboard.putNumber("Searching for target...", tx);
        }
    }
}