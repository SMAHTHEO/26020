package games.stendhal.server.entity.item.scroll;

import java.util.Map;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.events.TurnListener;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.npc.NPC;

/**
 * 表示减速卷轴的类。
 * 当使用此卷轴时，它将在玩家周围的一定范围内施加减速效果。
 */
public class SlownessScroll extends Scroll {

    private static final int EFFECT_RANGE = 5; // 减速效果的作用范围
    private static final int EFFECT_DURATION = 10; // 效果持续的回合数

    /**
     * 构造函数用于创建减速卷轴实例。
     *
     * @param name       卷轴名称
     * @param clazz      卷轴类别
     * @param subclass   卷轴子类
     * @param attributes 卷轴属性
     */
    public SlownessScroll(final String name, final String clazz, final String subclass,
                          final Map<String, String> attributes) {
        super(name, clazz, subclass, attributes);
    }

    /**
     * 复制构造函数用于创建卷轴的副本。
     *
     * @param item 要复制的卷轴对象
     */
    public SlownessScroll(final SlownessScroll item) {
        super(item);
    }

    /**
     * 当卷轴被使用时调用此方法。
     * 注册 SlownessEffectTurnListener 以在每个回合应用减速效果。
     *
     * @param player 使用卷轴的玩家
     * @return 如果成功使用则返回 true
     */
    @Override
    protected boolean useScroll(final Player player) {
        // 注册 SlownessEffectTurnListener，以便在每个回合应用减速效果
        SingletonRepository.getTurnNotifier().notifyInTurns(0, new SlownessEffectTurnListener(player, EFFECT_DURATION));
        return true;
    }

    /**
     * SlownessEffectTurnListener 类是 TurnListener 接口的实现，
     * 用于在减速卷轴使用后的每个回合应用减速效果。
     */
    class SlownessEffectTurnListener implements TurnListener {

        private final Player player;
        private int remainingTurns;

        SlownessEffectTurnListener(final Player player, final int duration) {
            this.player = player;
            this.remainingTurns = duration;
        }

        @Override
        public void onTurnReached(final int currentTurn) {
            if (remainingTurns > 0) {
                applySlownessEffect(player);
                remainingTurns--;
            } else {
                // 效果结束，移除监听器
            	SingletonRepository.getTurnNotifier().dontNotify(this);
            }
        }

        private void applySlownessEffect(final Player player) {
            // 获取玩家当前所在区域
            StendhalRPZone zone = player.getZone();

            // 获取玩家的坐标
            int playerX = player.getX();
            int playerY = player.getY();

            // 检查区域内的所有实体
            for (Entity entity : zone.getEntitiesAt(playerX, playerY)) {
                // 检查实体是否在指定范围内
                if (entity.squaredDistance(playerX, playerY) <= EFFECT_RANGE * EFFECT_RANGE) {
                    // 检查实体类型并应用减速效果
                    if (entity instanceof Player) {
                        // 对玩家应用减速效果
                        applyEffectToPlayer((Player) entity);
                    } else if (entity instanceof NPC) {
                        // 对NPC应用减速效果
                        applyEffectToNPC((NPC) entity);
                    }
                    // 可以继续添加对其他类型实体的处理
                }
            }
        }

        private void applyEffectToPlayer(Player player) {
            // 实现对玩家的减速效果
            // 可以是减少移动速度、攻击速度、回复速度等
        }

        private void applyEffectToNPC(NPC npc) {
            // 实现对NPC的减速效果
            // 可以是减少移动速度、攻击速度、回复速度等
        }

    }
}
