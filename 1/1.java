package games.stendhal.server.entity.item.scroll;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        private Set<Player> affectedPlayers; // 存储受影响的玩家

        SlownessEffectTurnListener(final Player sourcePlayer, final int duration) {
            this.sourcePlayer = sourcePlayer;
            this.remainingTurns = duration;
            this.affectedPlayers = new HashSet<>(); // 初始化集合
        }

        @Override
        public void onTurnReached(final int currentTurn) {
            // 检查是否到达效果持续的最后一回合
            if (remainingTurns > 0) {
                applySlownessEffect();
                remainingTurns--;
            } else {
                restoreSpeed(); // 效果结束，恢复所有受影响玩家的速度
                SingletonRepository.getTurnNotifier().dontNotify(this); // 移除监听器
            }
        }

        private void applySlownessEffect() {
            // 获取玩家当前所在区域
            StendhalRPZone zone = sourcePlayer.getZone();

            // 获取玩家的坐标
            int playerX = sourcePlayer.getX();
            int playerY = sourcePlayer.getY();

            // 遍历玩家周围的所有坐标
            for (int x = playerX - EFFECT_RANGE; x <= playerX + EFFECT_RANGE; x++) {
                for (int y = playerY - EFFECT_RANGE; y <= playerY + EFFECT_RANGE; y++) {
                    // 检查每个坐标点上的实体
                    for (Entity entity : zone.getEntitiesAt(x, y)) {
                        // 仅对玩家应用减速效果
                        if (entity instanceof Player && entity != sourcePlayer) {
                            Player affectedPlayer = (Player) entity;
                            affectedPlayers.add(affectedPlayer);
                            // 应用减速效果
                            affectedPlayer.setSlowDown(true);
                        }
                    }
                }
            }
        }

        private void restoreSpeed() {
            // 遍历并恢复受影响玩家的速度
            for (Player player : affectedPlayers) {
                player.setSlowDown(false);
            }
            affectedPlayers.clear(); // 清除集合，准备下一次使用
        }
    }
}
