import java.io.*;

/**
 * Created by suhas subramanya on 10/12/2015.
 */


public class mancala {
    static int[][] finalmatrix;
    static int max;
    public static void main(String []args) {
        File file = null;
        PrintWriter writefile = null;
        PrintWriter traverselogfile=null;
        if (args.length > 0) {
            file = new File(args[1]);
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\vatsa\\IdeaProjects\\mancala\\src\\input.txt"));
            writefile = new PrintWriter(new FileWriter("next_state.txt"));
            traverselogfile = new PrintWriter(new FileWriter("traverse_log.txt"));
            String line;
            mancalastate object=new mancalastate();
            line = br.readLine();
            object.taskNum=Integer.valueOf(line);
            line = br.readLine();
            object.player=Integer.valueOf(line);
            line = br.readLine();
            object.depth=Integer.valueOf(line);
            line = br.readLine();
            String []temp=line.split(" ");
            object.size=temp.length+2;
            object.mancmatrix=new int[2][object.size];
            for(int i=1;i<object.size-1;i++)
            {
                object.mancmatrix[0][i]=Integer.valueOf(temp[i-1]);
            }
            line = br.readLine();
            temp=line.split(" ");
            for(int i=1;i<object.size-1;i++)
            {
                object.mancmatrix[1][i]=Integer.valueOf(temp[i-1]);
            }
            line = br.readLine();
            object.mancmatrix[0][0]=Integer.valueOf(line);
            line = br.readLine();
            object.mancmatrix[1][object.size-1]=Integer.valueOf(line);
            max=Integer.MIN_VALUE;
            int originalPlayer=object.player;
            if(object.taskNum==2) {
                traverselogfile.println("Node,Depth,Value");
            }
            else if(object.taskNum==3)
            {
                traverselogfile.println("Node,Depth,Value");
            }
            if(object.taskNum==1) {

                calculateMinMax(object.mancmatrix, object.player, 0, 1,0,originalPlayer,1,object.taskNum,traverselogfile);

            }
            else if (object.taskNum==2)
            {
                calculateMinMax(object.mancmatrix, object.player, 0, object.depth, 0, originalPlayer, 1, object.taskNum, traverselogfile);
                System.out.println();


            }
            else
            {
                calculateMinMax(object.mancmatrix, object.player, 0, object.depth,0,originalPlayer,1,object.taskNum,traverselogfile);
            }

            for(int i=0;i<finalmatrix.length;i++)
            {
                for(int j=1;j<finalmatrix[0].length-1;j++)
                {
                    writefile.print(finalmatrix[i][j]);
                    writefile.print(" ");
                }
                writefile.println();
            }

            writefile.println(finalmatrix[0][0]);

            writefile.println(finalmatrix[1][finalmatrix[0].length - 1]);
            writefile.flush();
            traverselogfile.flush();
            writefile.close();
            traverselogfile.close();

        } catch (Exception ex) {

        }
    }

    static int min(Integer first,Integer second)
    {
        return first<second?first:second;
    }

    static int max(Integer first,Integer second)
    {
        return first>second?first:second;
    }

    static int calculateGreedy(int[][] origmatrix,int level, int value)
    {
        return 0;
    }

    static int evalFunction(int firstmancella,int secondmancella) {
        return firstmancella-secondmancella;
    }

    static int calculateMinMax(int[][] origmatrix,int player,int level, int depth, int truelevel,int originalplayer,int index,int tasknum,PrintWriter traverselogfile)
    {
        int returnvalue;
        if(level==0)
       {
           returnvalue=Integer.MIN_VALUE;

           if(tasknum==2) {
               printNodes(returnvalue, 0, 0, level, depth,traverselogfile);
           }
           int [][] returnstate=new int[2][];
           for(int i=1;i<origmatrix[0].length-1;i++)
           {
               int [][]tempmatrix=copyArray(origmatrix);
               int val=max(calculateMinMax(tempmatrix,player,level+1,depth-1,truelevel+1,originalplayer,i,tasknum,traverselogfile),returnvalue);
               if(val > returnvalue)
               {
                   returnstate =copyArray(finalmatrix);

               }
               returnvalue=val;
               if(tasknum==2) {
                   printNodes(returnvalue, 0, 0, level, depth,traverselogfile);
               }
           }
           finalmatrix=copyArray(returnstate);
       }
        else
        {
            if(player==originalplayer)
            {
                returnvalue=Integer.MAX_VALUE;
            }
            else
            {
                returnvalue=Integer.MIN_VALUE;
            }

            int length = origmatrix[0].length;
            int totallength = 2*origmatrix[0].length;

                boolean flag = false;
                int id=0;
                int[][] tempmatrix;
                if (player == 1) {

                    tempmatrix= copyArray(origmatrix);
                    id = index;
                    int pittoberemoved = tempmatrix[1][id];
                    tempmatrix[1][id] = 0;

                    /*if (pittoberemoved == 0) {
                        continue;
                    }*/
                    int valuetobeadded = pittoberemoved / (totallength - 3);
                    for (int j = id + 1; j < length-1; j++) {
                        int value = tempmatrix[1][j];
                        value = value + valuetobeadded;
                        tempmatrix[1][j] = value;

                    }
                    for (int j = length - 1; j > 0; j--) {
                        int value = tempmatrix[0][j];
                        value = value + valuetobeadded;
                        tempmatrix[0][j] = value;
                    }
                    if (pittoberemoved == totallength - 3) {

                        int value = tempmatrix[1][length - 1];
                        value = value + valuetobeadded + tempmatrix[0][id];
                        tempmatrix[0][id] = 0;
                        tempmatrix[1][id] = 0;
                        tempmatrix[1][length - 1] = value;
                    } else {
                        int remaning = pittoberemoved;
                        if(pittoberemoved>totallength-3) {
                            remaning= pittoberemoved - (totallength - 3);
                        }
                        //boolean flag = false;
                        while (remaning > 0) {
                            for (int j = id + 1; j <= length - 1; j++) {
                                if (tempmatrix[1][j] == 0 && remaning == 1 && j != length - 1) {
                                    int value = tempmatrix[1][tempmatrix[0].length-1];
                                    value=value+ remaning + tempmatrix[0][j];
                                    tempmatrix[1][length - 1] = value;
                                    tempmatrix[0][j] = 0;
                                    remaning = remaning - 1;


                                } else if (remaning == 1 && j==length-1) {
                                    tempmatrix[1][length - 1] = tempmatrix[1][length - 1] + 1;
                                    remaning = remaning - 1;
                                    flag = true;
                                } else {
                                    int value = tempmatrix[1][j];
                                    value = value + 1;
                                    tempmatrix[1][j] = value;
                                    remaning = remaning - 1;

                                }
                                if (remaning == 0)
                                    break;
                            }
                            if (remaning != 0) {
                                for (int k = length - 2; k > 0; k--) {

                                    int value = tempmatrix[0][k];
                                    value = value + 1;
                                    tempmatrix[0][k] = value;
                                    remaning = remaning - 1;
                                    if (remaning == 0) {
                                        break;
                                    }

                                }
                            }
                        }

                        if(originalplayer==1)
                        {
                            if(isEmpty(tempmatrix,1))
                                return evalFunction(tempmatrix[1][tempmatrix[0].length-1],tempmatrix[0][0]+finaltotalSum(tempmatrix,0));
                        }
                        else
                        {
                            if(isEmpty(tempmatrix,0))
                                return evalFunction(tempmatrix[0][0],tempmatrix[1][tempmatrix[0].length-1]+finaltotalSum(tempmatrix,1));
                        }
                    }

                } else {

                    tempmatrix = copyArray(origmatrix);
                    id = index;
                    int pittoberemoved = tempmatrix[0][id];
                    tempmatrix[0][id] = 0;
                    /*if (pittoberemoved == 0) {
                        continue;
                    }*/
                    int valuetobeadded = pittoberemoved / (totallength - 3);
                    for (int j = id-1; j >=0; j--) {
                        int value = tempmatrix[0][j];
                        value= value + valuetobeadded;
                        tempmatrix[0][j] = value;

                    }
                    for (int j = 1; j < length-1; j++) {
                        int value = tempmatrix[1][j];
                        value = value + valuetobeadded;
                        tempmatrix[1][j] = value;
                    }
                    for (int j = length-2; j >=id; j--) {
                        int value = tempmatrix[0][j];
                        value = value + valuetobeadded;
                        tempmatrix[0][j] = value;

                    }
                    if (pittoberemoved == totallength - 3) {

                        int value = tempmatrix[0][0];
                        value = value + valuetobeadded + tempmatrix[1][id];
                        tempmatrix[0][id] = 0;
                        tempmatrix[1][id] = 0;
                        tempmatrix[0][0] = value;
                    }
                    else {
                        int remaning = pittoberemoved;
                        if(pittoberemoved>totallength-3) {
                            remaning= pittoberemoved - (totallength - 3);
                        }

                        while (remaning > 0) {
                            for (int j = id - 1; j >=0; j--) {
                                if (tempmatrix[0][j] == 0 && remaning == 1 && j != 0) {
                                    int value = tempmatrix[0][0];
                                     value=value+ remaning + tempmatrix[0][j];
                                    tempmatrix[0][0] = value;
                                    tempmatrix[1][j] = 0;
                                    remaning = remaning - 1;

                                } else if (j==0 && remaning == 1) {
                                    tempmatrix[0][0] = tempmatrix[0][0] + 1;
                                    remaning = remaning - 1;
                                    flag = true;
                                } else {
                                    int value = tempmatrix[0][j];
                                    value = value + 1;
                                    tempmatrix[0][j] = value;
                                    remaning = remaning - 1;

                                }
                                if (remaning == 0)
                                    break;
                            }
                            if (remaning != 0) {
                                for (int k = 1; k < length - 1; k++) {

                                    int value = tempmatrix[1][k];
                                    value = value + 1;
                                    tempmatrix[1][k] = value;
                                    remaning = remaning - 1;
                                    if (remaning == 0) {
                                        break;
                                    }

                                }
                                if(remaning!=0) {
                                    for (int k = length - 2; k > id; k--) {
                                        int value = tempmatrix[0][k];
                                        value = value + 1;
                                        tempmatrix[0][k] = value;
                                        remaning = remaning - 1;
                                        if (remaning == 0) {
                                            break;
                                        }
                                    }
                                }
                            }

                        }
                        if(originalplayer==1)
                        {
                            if(isEmpty(tempmatrix,1))
                                return evalFunction(tempmatrix[1][tempmatrix[0].length-1],tempmatrix[0][0]+finaltotalSum(tempmatrix,0));
                        }
                        else
                        {
                            if(isEmpty(tempmatrix,0))
                                return evalFunction(tempmatrix[0][0],tempmatrix[1][tempmatrix[0].length-1]+finaltotalSum(tempmatrix,1));
                        }

                    }
                }
            if(level==1 && !flag)
            {
                finalmatrix=copyArray(tempmatrix);
            }
                if(depth==0&&!flag) {
                    if(originalplayer==1)
                    {

                        returnvalue= evalFunction(tempmatrix[1][tempmatrix[0].length-1],tempmatrix[0][0]);
                        if(tasknum==2) {
                            printNodes(returnvalue, player, id, level, depth,traverselogfile);
                        }

                        return returnvalue;
                    }
                    else
                    {

                        returnvalue= evalFunction(tempmatrix[0][0],tempmatrix[1][tempmatrix[0].length-1]);
                        if(tasknum==2) {
                            printNodes(returnvalue, player, id, level, depth,traverselogfile);
                        }

                        return returnvalue;
                    }

                }
            if(flag && depth==0)
            {
                if(originalplayer==1)
                {

                    returnvalue= evalFunction(tempmatrix[1][tempmatrix[0].length-1],tempmatrix[0][0]);

                }
                else
                {

                    returnvalue= evalFunction(tempmatrix[0][0],tempmatrix[1][tempmatrix[0].length-1]);

                }
            }
            if(returnvalue==Integer.MAX_VALUE||returnvalue==Integer.MIN_VALUE)
            {
                if(player==originalplayer && flag)
                {
                    returnvalue=Integer.MIN_VALUE;
                }
                else if(player==originalplayer && !flag)
                {
                    returnvalue=Integer.MAX_VALUE;
                }
                else if(player!=originalplayer && flag)
                {
                    returnvalue=Integer.MAX_VALUE;
                }
                else if(player!=originalplayer && !flag)
                {
                    returnvalue=Integer.MIN_VALUE;
                }
            }
            if(tasknum==2) {
                printNodes(returnvalue, player, id, level, depth,traverselogfile);
            }

            int [][]returnstate=new int[2][];
            for(int i=1;i<origmatrix[0].length-1;i++)
            {

                if(flag && (player==originalplayer))
                {
                    if(isCellEmpty(tempmatrix,player,i))
                    {
                        continue;
                    }
                    int val=max(calculateMinMax(tempmatrix,player,level,depth,truelevel+1,originalplayer,i,tasknum,traverselogfile),returnvalue);
                    if(val>returnvalue) {
                        if (level == 1 && flag)
                        {
                            returnstate=copyArray(finalmatrix);

                        }
                    }
                    returnvalue=val;
                }
                else if(!flag && (player==originalplayer))
                {
                    if(isCellEmpty(tempmatrix,player^3,i))
                    {
                        continue;
                    }

                    returnvalue = min(calculateMinMax(tempmatrix, player ^ 3, level + 1, depth - 1, truelevel + 1, originalplayer, i,tasknum,traverselogfile), returnvalue);


                }
                else if(flag && (player!=originalplayer))
                {
                    if(isCellEmpty(tempmatrix,player,i))
                    {
                        continue;
                    }
                    returnvalue=min(calculateMinMax(tempmatrix,player,level,depth,truelevel+1,originalplayer,i,tasknum,traverselogfile),returnvalue);
                }
                else if(!flag && (player!=originalplayer))
                {
                    if(isCellEmpty(tempmatrix,player^3,i))
                    {
                        continue;
                    }

                    returnvalue = max(calculateMinMax(tempmatrix, player ^ 3, level + 1, depth - 1, truelevel + 1, originalplayer,i,tasknum,traverselogfile),returnvalue);



                }

                if(tasknum==2) {
                    printNodes(returnvalue, player, id, level, depth,traverselogfile);
                }

            }
            if(returnstate[0]!=null) {
                finalmatrix = copyArray(returnstate);
            }
        }
        return returnvalue;
    }

    static void printNodes(Integer returnvalue,int player,Integer columnIndex,Integer level,int originalplayer,PrintWriter traverselogfile)
    {

        String value=returnvalue.toString();
        if (returnvalue==Integer.MAX_VALUE)
        {
            value= "Infinity";
        }
        else if (returnvalue==Integer.MIN_VALUE)
        {
            value= "-Infinity";
        }
        columnIndex=columnIndex+1;
        if(player==1)
        {
            traverselogfile.println("B" + columnIndex.toString() + ","+level.toString()+","+value);
        }
        else if(player==2)
        {
            traverselogfile.println("A"+columnIndex.toString()+","+level.toString()+","+value);
        }
        else
        {
            traverselogfile.println("root,"+"0"+","+value);
        }

    }

    static int finaltotalSum(int [][]tempmatrix, int rowindex)
    {
        int value=0;

        for(int i=1;i<tempmatrix[0].length-1;i++)
        {
            value=value+tempmatrix[rowindex][i];
        }
        return value;
    }

    static boolean isEmpty(int [][]tempmatrix, int rowindex)
    {
        for(int i=1;i<tempmatrix[0].length-1;i++)
        {
            if(tempmatrix[rowindex][i]!=0)
            {
                return false;
            }
        }
        return true;
    }

    static int[][] copyArray(int [][] origmatrix)
    {
        int[][]tempArray=new int[2][origmatrix[0].length];
        for(int i=0;i<2;i++)
        {
            for(int j=0;j<origmatrix[0].length;j++)
            {
                tempArray[i][j]=origmatrix[i][j];
            }
        }
        return tempArray;
    }
    static int calculateAlphaBeta(int[][] origmatrix,int level,  int value)
    {
        return 0;
    }

    static boolean isCellEmpty(int [][]origmatrix, int player, int colindex)
    {
        int rowindex=player==1?1:0;

        return ((origmatrix[rowindex][colindex]==0)?true:false);
    }
}

class mancalastate
{
    int taskNum;
    int depth;
    int player;
    int size;
    int [][]mancmatrix;
}




