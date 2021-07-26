using System;
using System.Security.Cryptography;
using System.Text;

namespace Game
{
    class Program
    {
        static void Main(string[] args)
        {
            string[] variants = { "Rock", "Scissors", "Paper" };
            string[] choices = { "Rock", "Scissors", "Paper", "Lizard", "Spock" };
            var key = new byte[128]; 
            var opponent = new byte[4]; 
            var gen = RandomNumberGenerator.Create(); 
            gen.GetBytes(key); 
            gen.GetBytes(opponent); 
            var hmac = new HMACSHA256(key); 
            var ipc = BitConverter.ToUInt32(opponent, 0) % (variants.Length); 
            var hash = hmac.ComputeHash(Encoding.UTF8.GetBytes(variants[ipc])); 
            Console.WriteLine("Computer made choice.\nHMAC : " + (BitConverter.ToString(hash, 0))); 
            Console.WriteLine("Your turn:\n0 : Exit"); 
            for (int i = 0; i < variants.Length; i++) Console.WriteLine((i + 1) + " : " + variants[i]); 
            int player = 0; 
            if (!int.TryParse(Console.ReadLine(), out player) || player < 0 || player > variants.Length) 
            { 
                Console.WriteLine("Incorrect entering. Enter the numbers that are in the list. Close the program and repeat again.");
                return;
            } 
            else if (player == 0) return; player -= 1; if (ipc == player) Console.WriteLine("Tie!"); 
            else if (((ipc + player) % 2 == 0 && ipc > player) || ((ipc + player) != 0 && player > ipc)) Console.WriteLine("You win!"); 
            else Console.WriteLine("You lose!"); 
            Console.WriteLine("Key : " + (BitConverter.ToString(hash, 0)));
            Console.WriteLine("-------------------------------------------");
            Console.WriteLine("Computer made choice.\nHMAC : " + (BitConverter.ToString(opponent, 0)));
            Console.WriteLine("Your turn:\n0 : Exit");
            for (int j = 0; j < choices.Length; j++) Console.WriteLine((j + 1) + " : " + choices[j]);
            int human = 0;
            if (!int.TryParse(Console.ReadLine(), out human) || human < 0 || human > choices.Length)
            {
                Console.WriteLine("Incorrect entering. Enter the numbers that are in the list. Close the program and repeat again.");
                return;
            }
            else if (human == 0) return; human -= 1; if (ipc == human) Console.WriteLine("Tie!");
            else if (((ipc + human) % 2 == 0 && ipc > human) || ((ipc + human) != 0 && human > ipc)) Console.WriteLine("You win!");
            else Console.WriteLine("You lose!");
            Console.WriteLine("Key : " + (BitConverter.ToString(opponent, 0)));
            Console.ReadLine(); 
        }
    }
}