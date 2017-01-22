using System.Text.RegularExpressions;

namespace Server_Is_NaN.Utils
{
    public class GeneralUtils
    {
        public static readonly Regex username_regex = new Regex("^(?=.{4,16}$)(?![_])(?!.*[_]{2})[a-zA-Z0-9_]+(?<![_])$");
    }
}
