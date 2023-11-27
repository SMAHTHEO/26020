给你一个java游戏项目的部分文件，根据要求 中文帮助我完整的构建新的文件 要求:As a developer, I like the idea of scrolls. They have a set duration and are a one-time use item - but they pretty much all teleport you or some pet/creature in some way or another.  We could use another kind of effect which would be beneficial to the players - like slowness for example.
Perhaps the slowness applies to creatures in a small range around the player for some amount of time. The creatures would move slower, attack slower, regenerate health slower etc. as if the player had slowed down time in a small vicinity around them!
This would really help players to manoeuvre through areas where there are loads of enemies. Or it could be used to aid their fight against enemies, as it would essentially weaken the enemies temporarily.
But then I guess that would be really strong. Maybe it should also slow down any other players or their own pets in the user's vicinity. This would mean that the scroll would have to be used at an appropriate place and time. A fun little idea too is that if the player accidentally used a scroll near NPCs, the NPCs would slow down too and it would take a longer time for the NPCs to respond to player conversations!
Creatures are already slower than players though, so slowing them even more at a fixed rate would make it too easy to evade them - which would be very strong for weaker players. Perhaps the effect of the slowness should depend on some property of the user such as level. If the player is lower level, the slowness effect is weaker, but if the player is higher level, the slowness effect is stronger?
I believe that the TimedTeleportScroll has implementation details for setting the duration of the scroll. Once the scroll is used, a new class which implements TurnListener is created which lasts for a specified number of turns - and once the turn is reached, the scroll is deactivated. For the idea of the scroll applying to a range around the player, another TurnListener class implementation can be used - but this time, this new TurnListener is created every turn (i.e. notify in 0 turns) which gives the effect that some code is constantly running every turn - this should be useful for constantly checking an area around the player. I think ShockStatusHandler has a similar approach for checking the infliction of the shock status effect.
Usually tests are run in a game environment that exists for one turn, but it will take another turn to inflict the slowness effect as a result of using a TurnListener. SpellTest has some code that forces the game environment's logic to run an extra turn to inflict a healing spell - perhaps this would be useful for testing the slowness scroll. 文件：package games.stendhal.server.entity.item.scroll;

import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import games.stendhal.common.Rand;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.events.TurnListener;
import games.stendhal.server.entity.player.Player;

/**
 * Represents a teleport scroll that takes the player to a specified location
 * for a specified time, after which it will teleport the player to given
 * location.
 * <p>
 * infostring attribute in items.xml:
 * <p>
 * <code> 1_dreamscape 77 35 5400 0_semos_plains_s -1 -1 </code>
 * <p>
 * where
 * <ul>
 * <li>1_dreamscape is the target zone name;
 * <li>77 and 35 are the target x and y position;
 * <li>5400 is the number of turns before return;
 * <li>0_semos_plains_s is the return zone;
 * <li>-1 and -1 are the return x and y positions (negative value means a random
 * position)
 * </ul>
 *
 * TODO: This class isn't fully self-containing as the LoginHandler (that
 * handles the players logging in the target zone) must be implemented
 * elsewhere, i.e. in a quest file.
 */
public class TimedTeleportScroll extends TeleportScroll {

	private static final Logger logger = Logger.getLogger(TimedTeleportScroll.class);

	/**
	 * Teleport the player back from the target zone.
	 *
	 * @param player
	 * @return true if teleport was successful
	 */
	public boolean teleportBack(final Player player) {
		String targetZoneName = null;
		String returnZoneName = null;
		int returnX = 0;
		int returnY = 0;
		final String infoString = getInfoString();
		if (infoString != null) {
			final StringTokenizer st = new StringTokenizer(infoString);
			if (st.countTokens() == 7) {
				targetZoneName = st.nextToken();

				// targetX
				st.nextToken();

				// targetY
				st.nextToken();

				// timeInTurns
				st.nextToken();
				returnZoneName = st.nextToken();
				returnX = Integer.parseInt(st.nextToken());
				returnY = Integer.parseInt(st.nextToken());
			} else {
				throw new IllegalArgumentException(
						"the infostring attribute is malformed");
			}
		}

		if ((player == null) || (player.getZone() == null)
				|| (targetZoneName == null)) {
			return true;
		}

		if (notInTargetZone(player, targetZoneName)) {
			return true;
		}

		final StendhalRPZone returnZone = SingletonRepository.getRPWorld().getZone(
				returnZoneName);

		int x = initCoord(returnX, returnZone.getWidth());
		int y = initCoord(returnY, returnZone.getHeight());

		final boolean result = player.teleport(returnZone, x, y, null, player);

		sendAfterTransportMessage(player);

		return result;
	}

	private boolean notInTargetZone(final Player player, final String targetZoneName) {
		return !targetZoneName.equals(player.getZone().getName());
	}

	private void sendAfterTransportMessage(final Player player) {
		final String afterReturnMessage = getAfterReturnMessage();
		if (afterReturnMessage != null) {
			player.sendPrivateText(afterReturnMessage);
		}
	}

	/**
	 * Evaluates the given coord to be non negative.
	 *
	 * @param coord
	 * @param max
	 * @return the coord if coord non negative or a randomized value between 0 and max.
	 */
	private int initCoord(final int coord, final int max) {
		int x;
		if (coord < 0) {
			x = Rand.rand(max);
		} else {
			x = coord;
		}
		return x;
	}

	/**
	 * Creates a new timed marked teleport scroll.
	 *
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public TimedTeleportScroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	/**
	 * Copy constructor.
	 *
	 * @param item
	 *            item to copy
	 */
	public TimedTeleportScroll(final TimedTeleportScroll item) {
		super(item);
	}

	/**
	 * Is invoked when a teleporting scroll is used. Tries to put the player on
	 * the scroll's destination, or near it.
	 *
	 * @param player
	 *            The player who used the scroll and who will be teleported
	 * @return true if teleport was successful
	 */
	@Override
	protected boolean useTeleportScroll(final Player player) {
		String targetZoneName = null;
		int targetX = 0;
		int targetY = 0;
		int timeInTurns = 0;
		final String infoString = getInfoString();
		if (infoString != null) {
			final StringTokenizer st = new StringTokenizer(infoString);
			if (st.countTokens() == 7) {
				targetZoneName = st.nextToken();
				targetX = Integer.parseInt(st.nextToken());
				targetY = Integer.parseInt(st.nextToken());
				timeInTurns = Integer.parseInt(st.nextToken());
			} else {
				throw new IllegalArgumentException(
						"the infostring attribute is malformed");
			}
		}

		return useTeleportScroll(player, targetZoneName, targetX, targetY, timeInTurns);
	}

	/**
	 * Is invoked when a teleporting scroll is used. Tries to put the player on
	 * the destination, or near it.
	 *
	 * @param player
	 * 	The player who used the scroll
	 * @param targetZoneName
	 * 	The name of the zone where the player tries to teleport
	 * @param x
	 * 	x coordinate of the target location
	 * @param y
	 * 	y coordinate of the target location
	 * @param timeInTurns
	 * 	The time on turns that the player should spend on the the target
	 * 	zone unless she leaves by other means than the scrolls timeout feature
	 * @return true if teleport was succesful
	 */
	protected boolean useTeleportScroll(final Player player, final String targetZoneName,
			final int x, final int y, int timeInTurns) {
		final StendhalRPZone targetZone = SingletonRepository.getRPWorld().getZone(
				targetZoneName);

		if (targetZone == null) {
			logUnknownZone(targetZoneName);
			return false;
		} else {
			createWarningBeforeRetransport(player, targetZoneName, timeInTurns);
			createReTransportTimer(player, timeInTurns);

			return teleportPlayer(player, x, y, targetZone);
		}
	}

	/**
	 * Teleports the player to the given position in the given zone.
	 * Uses player as teleporter to give report to him in case something goes wrong while transport.
	 *
	 * @param player the person to teleport
	 * @param targetX
	 * @param targetY
	 * @param targetZone the zone to teleport to.
	 * @return true if successful
	 */
	private boolean teleportPlayer(final Player player, final int targetX,
			final int targetY, final StendhalRPZone targetZone) {

		return player.teleport(targetZone, targetX, targetY, null, player);
	}

	private void createReTransportTimer(final Player player, final int timeInTurns) {
		SingletonRepository.getTurnNotifier().notifyInTurns(timeInTurns,
				new TimedTeleportTurnListener(player));
	}

	private void logUnknownZone(final String targetZoneName) {
		logger.warn("Timed teleport scroll to unknown zone: " + targetZoneName);
	}

	private void createWarningBeforeRetransport(final Player player,
			final String targetZoneName, final int timeInTurns) {
		final String beforeReturnMessage = getBeforeReturnMessage();
		if (beforeReturnMessage != null) {
			SingletonRepository.getTurnNotifier().notifyInTurns(
					(int) (timeInTurns * 0.9),
					new TimedTeleportWarningTurnListener(player,
							SingletonRepository.getRPWorld().getZone(targetZoneName),
							beforeReturnMessage));
		}
	}

	/**
	 * override this to show a message before teleporting the player back.
	 *
	 * @return the message to shown or null for no message
	 */
	protected String getBeforeReturnMessage() {
		return null;
	}

	/**
	 * override this to show a message after teleporting the player back.
	 *
	 * @return the message to shown or null for no message
	 */
	protected String getAfterReturnMessage() {
		return null;
	}

	/**
	 * TimedTeleportTurnListener class is the implementation of the TurnListener
	 * interface for the timed teleport.
	 */
	class TimedTeleportTurnListener implements TurnListener {

		private final Player player;

		TimedTeleportTurnListener(final Player player) {
			this.player = player;
		}

		@Override
		public void onTurnReached(final int currentTurn) {
			teleportBack(player);
		}
	}

	/**
	 * TimedTeleportWarningTurnListener class is the implementation of the
	 * TurnListener interface for the timed teleport to send a warning message
	 * to the player before teleporting back.
	 */
	static class TimedTeleportWarningTurnListener implements TurnListener {

		private final Player player;
		private final StendhalRPZone zone;
		private final String warningMessage;

		TimedTeleportWarningTurnListener(final Player player, final StendhalRPZone zone,
				final String warningMessage) {
			this.player = player;
			this.zone = zone;
			this.warningMessage = warningMessage;
		}

		@Override
		public void onTurnReached(final int currentTurn) {
			if ((player == null) || (player.getZone() == null) || (zone == null)) {
				return;
			}
			if (player.getZone().getName().equals(zone.getName())) {
				player.sendPrivateText(warningMessage);
			}
		}
	}

}package games.stendhal.server.entity.item.scroll;

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
}package games.stendhal.server.core.events;

/**
 * Implementing classes can be notified that a certain turn number has been
 * reached.
 *
 * After registering at the TurnNotifier, the TurnNotifier will wait until the
 * specified turn number has been reached, and notify the TurnListener.
 *
 * A string can be passed to the TurnNotifier while registering; this string
 * will then be passed back to the TurnListener when the specified turn number
 * has been reached. Using this string, a TurnListener can register itself
 * multiple times at the TurnNotifier.
 *
 * @author hendrik
 */
public interface TurnListener {
	/**
	 * This method is called when the turn number is reached.
	 *
	 * @param currentTurn
	 *            current turn number
	 */
	void onTurnReached(int currentTurn);
}package games.stendhal.server.entity.item.scroll;

import java.util.Map;

import org.apache.log4j.Logger;

import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPObject;

/**
 * Represents a scroll.
 */
public class Scroll extends StackableItem {

	private static final Logger logger = Logger.getLogger(Scroll.class);

	/**
	 * Creates a new scroll.
	 *
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public Scroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	/**
	 * Copy constructor.
	 *
	 * @param item
	 *            item to copy
	 */
	public Scroll(final Scroll item) {
		super(item);
	}

	@Override
	public final boolean onUsed(final RPEntity user) {
		RPObject base = getBaseContainer();

		if (user.nextTo((Entity) base)) {
			// We need to remove the scroll before using it. Makes space in
			// the bag in the case of last empty scrolls, and prevents
			// the player getting free replacement scrolls from bank vaults.
			// Save the necessary information for backtracking:
			Scroll clone = (Scroll) clone();
			Scroll splitted = (Scroll) splitOff(1);
			StendhalRPZone zone = getZone();

			if (user instanceof Player && useScroll((Player)user)) {
				user.notifyWorldAboutChanges();
				return true;
			} else {
				if (getQuantity() != 0) {
					// Return what we just failed to use
					add(splitted);
				} else {
					// Used the last scroll, but failed. Return the
					// scroll to where it used to be
					if (clone.isContained()) {
						clone.getContainerSlot().add(clone);
					} else {
						// unset the zone first to avoid it looking like adding it to two zones
						clone.onRemoved(zone);
						zone.add(clone);
					}
				}

				return false;
			}
		} else {
			logger.debug("Scroll is too far away.");
			return false;
		}
	}

	/**
	 * Use a scroll.
	 *
	 * @param player
	 *            The player using scroll.
	 *
	 * @return <code>true</code> if successful, <code>false</code>
	 *         otherwise.
	 */
	protected boolean useScroll(final Player player) {
		player.sendPrivateText("What a strange scroll! You can't make heads or tails of it.");
		return false;
	}

}
package games.stendhal.server.entity.item.scroll;

import java.util.Map;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;

/**
 * Represents an empty/unmarked teleport scroll.
 */
public class EmptyScroll extends Scroll {

	// private static final Logger logger = Logger.getLogger(EmptyScroll.class);

	/**
	 * Creates a new empty scroll.
	 *
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public EmptyScroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	/**
	 * Copy constructor.
	 *
	 * @param item
	 *            item to copy
	 */
	public EmptyScroll(final EmptyScroll item) {
		super(item);
	}

	/**
	 * Use a [empty] scroll.
	 *
	 * @param player
	 * @return always true
	 */
	@Override
	protected boolean useScroll(final Player player) {
		final StendhalRPZone zone = player.getZone();

		if (zone.isTeleportInAllowed(player.getX(), player.getY())) {
			final Item markedScroll = SingletonRepository.getEntityManager().getItem(
					"marked scroll");
			markedScroll.setInfoString(player.getID().getZoneID() + " "
					+ player.getX() + " " + player.getY());
			player.equipOrPutOnGround(markedScroll);
			return true;
		} else {
			player.sendPrivateText("The strong anti magic aura in this area prevents the scroll from working!");
			return false;
		}
	}
}
