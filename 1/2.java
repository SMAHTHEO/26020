package games.stendhal.server.entity.item.scroll;

import java.util.Map;

import games.stendhal.common.MathHelper;
import games.stendhal.server.core.events.DelayedPlayerTextSender;
import games.stendhal.server.entity.player.Player;

/**
 * Represents the balloon that takes the player to 7 kikareukin clouds,
 * after which it will teleport player to a random location in 6 kikareukin islands.
 */
public class BalloonScroll extends TimedTeleportScroll {

	private static final long DELAY = 6 * MathHelper.MILLISECONDS_IN_ONE_HOUR;
	private static final int NEWTIME = 540;

	/**
	 * Creates a new timed marked BalloonScroll scroll.
	 *
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public BalloonScroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	/**
	 * Copy constructor.
	 *
	 * @param item
	 *            item to copy
	 */
	public BalloonScroll(final BalloonScroll item) {
		super(item);
	}

	@Override
	protected String getBeforeReturnMessage() {
		return "It feels like the clouds won't take your weight much longer ... ";
	}

	@Override
	protected String getAfterReturnMessage() {
		return "You fell through a hole in the clouds, back to solid ground.";
	}

	// Only let player use balloon from 6 kika clouds
	// Balloons used more frequently than every 6 hours only last 5 minutes
	@Override
	protected boolean useTeleportScroll(final Player player) {
		if (!"6_kikareukin_islands".equals(player.getZone().getName())) {
			if ("7_kikareukin_clouds".equals(player.getZone().getName())) {
				player.sendPrivateText("Another balloon does not seem to lift you any higher.");
			} else {
				player.sendPrivateText("The balloon tried to float you away but the altitude was too low for it to even lift you. "
						+ "Try from somewhere higher up.");
			}
			return false;
		}
		long lastuse = -1;
		if (player.hasQuest("balloon")) {
			lastuse = Long.parseLong(player.getQuest("balloon"));
		}

		player.setQuest("balloon", Long.toString(System.currentTimeMillis()));

		final long timeRemaining = (lastuse + DELAY) - System.currentTimeMillis();
		if (timeRemaining > 0) {
			// player used the balloon within the last DELAY hours
			// so this use of balloon is going to be shortened
			// (the clouds can't take so much weight on them)
			// delay message for 1 turn for technical reasons
			new DelayedPlayerTextSender(player, "The clouds are weakened from your recent time on them, and will not hold you for long.", 1);

			return super.useTeleportScroll(player, "7_kikareukin_clouds", 31, 21, NEWTIME);
		}

		return super.useTeleportScroll(player);
	}
}

package games.stendhal.server.entity.item.scroll;

import java.util.Map;

import games.stendhal.common.MathHelper;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;

/**
 * Represents the rainbow beans that takes the player to the dream world zone,
 * after which it will teleport player to a random location in 0_semos_plains_s.
 */
public class RainbowBeansScroll extends TimedTeleportScroll {

	private static final long DELAY = 6 * MathHelper.MILLISECONDS_IN_ONE_HOUR;

	/**
	 * Creates a new timed marked RainbowBeansScroll scroll.
	 *
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public RainbowBeansScroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	/**
	 * Copy constructor.
	 *
	 * @param item
	 *            item to copy
	 */
	public RainbowBeansScroll(final RainbowBeansScroll item) {
		super(item);
	}

	@Override
	protected boolean useTeleportScroll(final Player player) {
		final String QUEST_SLOT = "rainbow_beans";
		long lastuse = -1;
		if (player.hasQuest(QUEST_SLOT)) {
			final String[] tokens = player.getQuest(QUEST_SLOT).split(";");
			if (tokens.length == 4) {
				// we stored a last time (or -1)
				lastuse = Long.parseLong(tokens[3]);
			}
			final long timeRemaining = (lastuse + DELAY) - System.currentTimeMillis();
			if (timeRemaining > 0) {
				// player used the beans within the last DELAY hours
				// so are not allowed to go yet. but don't reset the last time taken.
				// the private text doesn't get sent because events are lost on zone change. (marauroa bug)
				player.sendPrivateText("You were just sick from overuse of the rainbow beans. Classy!");
				final Item sick = SingletonRepository.getEntityManager().getItem("vomit");
				player.getZone().add(sick);
				sick.setPosition(player.getX(), player.getY() + 1);
				// Success, so that the beans still gets used up, even though
				// the player was not teleported.
				return true;
			} else {
				// don't overwrite the last bought time from Pdiddi, this is in tokens[1]
				player.setQuest(QUEST_SLOT, "bought;" + tokens[1] + ";taken;" + System.currentTimeMillis());
				return super.useTeleportScroll(player);
			}
		} else {
			// players can only buy rainbow beans from Pdiddi who stores the time bought in quest slot
			// so if they didn't have the quest slot they got the beans ''illegally''
			player.sendPrivateText("Those dodgy beans made you sick. Next time buy them from Pdiddi.");
			this.removeOne();
			final Item sick = SingletonRepository.getEntityManager().getItem("vomit");
			player.getZone().add(sick);
			sick.setPosition(player.getX(), player.getY() + 1);
			return false;
		}
	}

	@Override
	protected String getBeforeReturnMessage() {
		return "Your head begins to feel clearer...";
	}

	@Override
	protected String getAfterReturnMessage() {
		return "You find yourself in the forest with a bad headache."
				+ " That was a strange experience.";
	}
}

package games.stendhal.server.entity.item.scroll;

import java.util.Map;

/**
 * Represents the balloon that takes the player to twilight zone,
 * after which it will teleport player to a random location in ida's sewing room.
 */
public class TwilightMossScroll extends TimedTeleportScroll {

	/**
	 * Creates a new timed marked TwilightMossScroll scroll.
	 *
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public TwilightMossScroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	/**
	 * Copy constructor.
	 *
	 * @param item
	 *            item to copy
	 */
	public TwilightMossScroll(final TwilightMossScroll item) {
		super(item);
	}

	@Override
	protected String getBeforeReturnMessage() {
		return "The twilight is dwindling ...";
	}

	@Override
	protected String getAfterReturnMessage() {
		return "You wake up back in Ida's familiar sewing room.";
	}
}
