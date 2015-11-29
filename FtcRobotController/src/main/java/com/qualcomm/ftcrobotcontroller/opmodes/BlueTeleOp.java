public class BlueTeleOp extends TeleOp
{
	@Override
	public void runOpMode()
	{
		isRedSide = false;
		isBlueSide = true;
		super.runOpMode();
	}
}