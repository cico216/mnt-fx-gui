package load;

import com.mnt.gui.fx.loader.classload.ClassKey;
import com.mnt.gui.fx.test.load.TestClass;

/**
 * 
 * <p>
 * 测试类1 
 * </p>
 * @author    mnt.cico
 * @version  2015年5月24日 下午11:59:48 mnt.cico .
 * @since   FX8.0
 */
public class Load2 extends TestClass implements ClassKey {

	@Override
	public String getValue() {
		return "load2";
	}

	@Override
	public String getKey() {
		return "key2";
	}
}
