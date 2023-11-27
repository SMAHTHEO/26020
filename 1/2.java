package games.stendhal.server.entity.item.scroll;

import games.stendhal.server.entity.player.Player;
import utilities.PlayerTestHelper;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.events.TurnNotifier;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SlownessScrollTest {

    private Player player;
    private StendhalRPZone testZone;
    private TurnNotifier turnNotifier;

    @Before
    public void setUp() {
        player = PlayerTestHelper.createPlayer("bob");
        testZone = new StendhalRPZone("test_zone");
        turnNotifier = SingletonRepository.getTurnNotifier();
        player.setPosition(5, 5); // 直接设置玩家位置
    }

    /**
     * 测试使用 SlownessScroll 是否正确注册 TurnListener 并应用减速效果。
     */
    @Test
    public void testApplySlownessEffect() {
        // 创建测试NPC和玩家，并添加到区域
        Player otherPlayer = PlayerTestHelper.createPlayer("other_player");
        testZone.add(otherPlayer);

        // 设置实体的位置
        otherPlayer.setPosition(7, 7);

        // 使用减速卷轴
        SlownessScroll scroll = new SlownessScroll("Slow Scroll", "scroll", "slowness", null);
        scroll.useScroll(player);

        // 验证减速效果是否正确应用
        // 注意：此处需要根据游戏的具体逻辑来检测减速效果
        assertTrue("其他玩家应受到减速效果", isPlayerSlowed(otherPlayer));
    }

    private boolean isPlayerSlowed(Player player) {
    	return false;
        // 此处应实现检测玩家是否减速的逻辑
        // 假设玩家已经减速
    }

}
