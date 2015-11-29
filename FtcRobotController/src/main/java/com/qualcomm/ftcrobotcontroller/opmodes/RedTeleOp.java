public class RedTeleOp extends TeleOp
{
	@Override
	public void runOpMode()
	{
		isRedSide = true;
		isBlueSide = false;
		super.runOpMode();
	}
}