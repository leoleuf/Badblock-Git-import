using System.IO;
using Newtonsoft.Json;
using System;

namespace Server_Is_NaN.Utils
{
    public class JsonUtils
    {
        public static T LoadFile<T>(string fileName)
        {
            if (!File.Exists(fileName))
                File.Create(fileName).Close();

            string text = File.ReadAllText(fileName);

            T obj = default(T);

            try
            {
                obj = JsonConvert.DeserializeObject<T>(text);
            }
            catch (Exception) { }

            if (obj == null)
                obj = JsonConvert.DeserializeObject<T>("{}");

            return obj;
        }

        public static void SaveFile(string fileName, object value, bool indented)
        {
            string val = JsonConvert.SerializeObject(value, indented ? Formatting.Indented : Formatting.None);
            File.WriteAllText(fileName, val);
        }
    }
}
