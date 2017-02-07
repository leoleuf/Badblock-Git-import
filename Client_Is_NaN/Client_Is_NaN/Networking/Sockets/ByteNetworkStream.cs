using System;
using System.Text;
using System.Net.Sockets;
using System.IO;
using Ionic.Zlib;
using System.Collections.Generic;

namespace Server_Is_NaN.Networking.Sockets
{
    public class ByteNetworkStream
    {
        private MemoryStream writerMemory;
        private Stream writer;
        private Stream reader;

        private Stream stream;

        private bool compressed;

        public ByteNetworkStream(Socket socket, bool compressed)
        {
            this.compressed = compressed;

            writerMemory = new MemoryStream();
            writer = writerMemory;
            reader = new NetworkStream(socket);
            stream = new NetworkStream(socket);

            if (compressed)
            {
                writer = new ZlibStream(writerMemory, CompressionMode.Compress, true);
                //((ZlibStream)writer).FlushMode = FlushType.Finish;

                reader = new ZlibStream(reader, CompressionMode.Decompress);
            }
        }

        //private Stream writingStream, readingStream;

        public int ReadByte()
        {
            return reader.ReadByte();
        }

        public void WriteByte(byte val)
        {
            writer.WriteByte(val);
        }

        public int ReadUnsignedByte()
        {
            return ReadByte() & 0xFF;
        }

        public byte[] ReadByteArray()
        {
            return ReadBytes(ReadInt());
        }

        public byte[] ReadBytes(int length)
        {
            byte[] res = new byte[length];
            int beg = 0;

            while (beg < length)
            {
                beg += reader.Read(res, beg, length - beg);
            }

            return res;
        }

        public int ReadInt()
        {
            return ReadInt(5);
        }

        public int ReadInt(int maxBytes)
        {
            int result = 0, bytes = 0;
            byte i;

            do
            {
                i = (byte)ReadByte();
                result |= (i & 0x7F) << (bytes++ * 7);

                if (bytes > maxBytes)
                    throw new Exception("VarInt too big");
            } while ((i & 0x80) == 0x80);

            return result;
        }

        public long ReadLong()
        {
            byte[] bytes = ReadBytes(8);
            long result = 0;

            for (int i = 0; i < bytes.Length; i++)
            {
                result <<= 8;
                result |= (long)bytes[i] & 0xFF;
            }

            return result;
        }

        public float ReadFloat()
        {
            return BitConverter.ToSingle(ReadBytes(4), 0);
        }

        public double ReadDouble()
        {
            return BitConverter.Int64BitsToDouble(ReadLong());
        }

        public bool ReadBoolean()
        {
            return ReadByte() == 1;
        }

        public string ReadUTF()
        {
            return System.Text.Encoding.UTF8.GetString(ReadByteArray());
        }

        public T[] ReadArray<T>(Func<T> method)
        {
            T[] res = new T[ReadInt()];

            for (int i = 0; i < res.Length; i++)
                res[i] = method();

            return res;
        }

        public void WriteUnsignedByte(int value)
        {
            WriteByte((byte)value);
        }

        public void WriteBytes(byte[] bytes)
        {
            foreach (byte b in bytes)
                WriteByte(b);
        }

        public void WriteInt(int value)
        {
            int part;

            do
            {
                part = value & 0x7F;

                value >>= 7;
                if (value != 0)
                {
                    part |= 0x80;
                }

                WriteByte((byte)part);
            } while (value != 0);
        }

        public void WriteLong(long l)
        {
            WriteBytes(_WriteBigNumber(l, 8));
        }

        public unsafe void WriteFloat(float f)
        {
            int val = *((int*)&f);
            WriteInt(val);
        }

        public double WriteDouble()
        {
            return BitConverter.Int64BitsToDouble(ReadLong());
        }

        public void WriteBoolean(bool b)
        {
            WriteByte(b ? (byte)1 : (byte)0);
        }

        public void WriteUTF(string s)
        {
            WriteArray(WriteByte, Encoding.UTF8.GetBytes(s));
        }

        public void WriteArray<T>(Action<T> method, T[] r)
        {
            WriteInt(r.Length);
            foreach (T e in r)
                method.Invoke(e);
        }

        private static byte[] _WriteBigNumber(long value, int bytes)
        {
            byte[] result = new byte[bytes];

            for (int i = 0; i < bytes; i++)
                result[bytes - i - 1] = (byte)((value >> 8 * i) & 0xFF);

            return result;
        }

        public void Flush()
        {
            if (writer is ZlibStream)
            {
                ZlibStream stream = (ZlibStream)writer;
                stream.Close();
            }

            long len = writerMemory.Length;
            byte[] res = writerMemory.GetBuffer();

            writerMemory.Close();

            writerMemory = new MemoryStream();
            writer = writerMemory;

            if (compressed)
            {
                writer = new ZlibStream(writerMemory, CompressionMode.Compress, true);
            }

            stream.Write(res, 0, (int)len);
            stream.Flush();
        }
    }
}
