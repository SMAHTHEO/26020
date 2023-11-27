package games.stendhal.server.entity.item.scroll;

import java.util.HashMap;
import java.util.Map;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.events.TurnListener;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.Entity;

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

        private final Player sourcePlayer;
        private int remainingTurns;
        private Map<Player, Double> originalSpeeds; // 存储受影响玩家的原始速度

        SlownessEffectTurnListener(final Player sourcePlayer, final int duration) {
            this.sourcePlayer = sourcePlayer;
            this.remainingTurns = duration;
            this.originalSpeeds = new HashMap<>(); // 初始化映射
        }

        @Override
        public void onTurnReached(final int currentTurn) {
            if (remainingTurns > 0) {
                applySlownessEffect(sourcePlayer);
                remainingTurns--;
            } else {
                // 效果结束，恢复所有受影响玩家的速度，并移除监听器
                for (Map.Entry<Player, Double> entry : originalSpeeds.entrySet()) {
                    entry.getKey().setSpeed(entry.getValue());
                }
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
                        Player affectedPlayer = (Player) entity;
                        // 第一次减速时存储原始速度
                        if (!originalSpeeds.containsKey(affectedPlayer)) {
                            originalSpeeds.put(affectedPlayer, affectedPlayer.getSpeed());
                            affectedPlayer.setSpeed(0.6 * affectedPlayer.getSpeed()); // 减速
                        }
                    }
                }
            }
        }
    }

}
