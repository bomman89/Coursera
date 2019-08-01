import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront 
{
    private static class Node
    {
        public Node next;
        public Node prev;
        public char ch;
        
        public Node(char c)
        {
            next=null;
            prev=null;
            ch=c;
        }
    }
    
    private static Node list = null;
    private static void Remove(Node node)
    {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = null;
        node.prev = null;
    }
    
    private static void InsertAtHead(Node node)
    {
        node.next = list.next;
        node.prev = list;
        list.next.prev = node;        
        list.next = node;
    }
    
    private static int Search(char c)
    {
        if (list.next == list)
        {
            InsertAtHead(new Node(c));
            return -1;
        }
        
        Node next = list.next;
        int position = 0;
        
        // StdOut.println("Find "+c);
        while (next != list)
        {
            // StdOut.println(next.ch+" ");
            if (next.ch == c) 
            {
                Remove(next);
                // PrintList();
                InsertAtHead(next);
                // StdOut.println(next.ch+" "+c);
                return position;
            }
            next = next.next;
            position++;
        }
        
        StdOut.println("Miss");
        InsertAtHead(new Node(c));
        return -1;
    }

    private static void PrintList()
    {
        Node next = list.next;
        
        StdOut.println("Print List");
        while (next != list)
        {
            StdOut.print(next.ch+" ");  
            next = next.next;
        }
        
        StdOut.println();
        
    }
    private static char Search(int position)
    {   
        Node next = list.next;
        int currPosition=0;
        while (next != list)
        {
            if (position==currPosition) 
            {
                Remove(next);
                InsertAtHead(next);
                // PrintList();
                return (next.ch);
            }
            next = next.next;
            currPosition++;
        }
        
        return ('\0');
    }    
    
    private static void InitList()
    {
        list = new Node('\0');
        list.next = list;
        list.prev = list;
      
        int ch;
        for (ch=255; ch>=0; ch--)
            InsertAtHead(new Node((char)ch));   
    }
    
    public MoveToFront()
    {
        InitList();
        // PrintList();
    }
    
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode()
    {
        String str = BinaryStdIn.readString();
        int idx = 0;
        int strLength = str.length();
        InitList();
        
        while (idx < strLength)
        {
            char ch = str.charAt(idx);
            int position = Search(ch);
            if (position != -1)
                BinaryStdOut.write((byte)position);
            else
                BinaryStdOut.write((byte)ch);
            idx++;
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
        String str = BinaryStdIn.readString();
        int idx=0;
        int strLength = str.length();
        InitList();
        
        while (idx < strLength)
        {
            char ch = Search((int)(str.charAt(idx)));
            BinaryStdOut.write(ch);
            idx++;
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args)
    {
        MoveToFront moveToFrontObj = new MoveToFront();
        
        if (args[0].compareTo("-") == 0)
            MoveToFront.encode();
        else if (args[0].compareTo("+") == 0)
            MoveToFront.decode();
    }
}