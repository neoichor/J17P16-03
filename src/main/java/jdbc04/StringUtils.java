package jdbc04;

/**
 * 文字列処理に関する共通処理をまとめたユーティリティクラスです。
 */
public class StringUtils {
	// /**
	//  * 文字列がnullの場合に空文字列を返します。
	//  * 表示用途でのNullPointerExceptionを防ぎ、null値を安全に扱いたい場合に利用します。
	//  *
	//  * @param str 対象文字列
	//  * @return nullの場合は空文字列、それ以外は元の文字列
	//  */
	// public static String getSafeString(String str) {
	// 	return str != null ? str : "";
	// }

	/**
	 * 文字列がnull、空、または空白文字のみで構成されているかを判定します。
	 * 入力値のバリデーションや、ブランク判定が必要な場面で汎用的に利用できます。
	 *
	 * @param str 対象文字列
	 * @return null、空、または空白文字のみの場合はtrue、それ以外はfalse
	 */
	public static boolean isNullOrBlank(String str) {
		return str == null || str.trim().isEmpty();
	}
}
