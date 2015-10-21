import javax.swing.text.DefaultEditorKit;
import java.io.*;

/**
 * Created by suhas subramanya on 10/12/2015.
 */


public class mancala {
    static int[][] finalmatrix;
    static int count=0;
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
            int originalPlayer=object.player;
            if(object.taskNum==2) {
                traverselogfile.print("Node,Depth,Value");
            }
            else if(object.taskNum==3)
            {
                traverselogfile.print("Node,Depth,Value,Alpha,Beta");
            }
            if(object.taskNum==1) {

                calculateMinMax(object.mancmatrix, object.player, 0, 1,0,originalPlayer,1,object.taskNum,traverselogfile,null,null);

            }
            else if (object.taskNum==2)
            {
                calculateMinMax(object.mancmatrix, object.player, 0, object.depth, 0, originalPlayer, 1, object.taskNum, traverselogfile,null,null);



            }
            else
            {
                calculateMinMax(object.mancmatrix, object.player, 0, object.depth,0,originalPlayer,1,object.taskNum,traverselogfile,Integer.MIN_VALUE,Integer.MAX_VALUE);
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

            writefile.print(finalmatrix[0][0]);

            writefile.print("\n"+finalmatrix[1][finalmatrix[0].length - 1]);
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

    static int evalFunction(int firstmancella,int secondmancella) {
        return firstmancella-secondmancella;
    }

    static int calculateMinMax(int[][] origmatrix,int player,int level, int depth, int truelevel,int originalplayer,int index,int tasknum,PrintWriter traverselogfile,Integer alpha,Integer Beta)
    {
        int returnvalue;
        if(level==0)
       {
           returnvalue=Integer.MIN_VALUE;

           if(tasknum==2 || tasknum==3) {
               printNodes(returnvalue, 0, 0, level, depth,traverselogfile,alpha,Beta);
           }
           int [][] returnstate=new int[2][];
           for(int i=1;i<origmatrix[0].length-1;i++)
           {
               int [][]tempmatrix=copyArray(origmatrix);
               if(player==1)
               {
                   if(tempmatrix[1][i]==0)
                   {
                       continue;
                   }
               }
               else {
                   if(tempmatrix[0][i]==0)
                   {
                       continue;
                   }
               }
               int val=max(calculateMinMax(tempmatrix,player,level+1,depth-1,truelevel+1,originalplayer,i,tasknum,traverselogfile,alpha,Beta),returnvalue);
               if(val > returnvalue)
               {
                   returnstate =copyArray(finalmatrix);

               }
               returnvalue=val;
               if(tasknum==3)
               {
                   if(val>=Beta)
                   {
                       return val;
                   }
                   alpha=max(alpha,val);
               }

               if(tasknum==2 || tasknum==3) {
                   printNodes(returnvalue, 0, 0, level, depth,traverselogfile,alpha,Beta);
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
                    for (int j = id + 1; j < length; j++) {
                        int value = tempmatrix[1][j];
                        value = value + valuetobeadded;
                        tempmatrix[1][j] = value;

                    }
                    for (int j = length - 2; j > 0; j--) {
                        int value = tempmatrix[0][j];
                        value = value + valuetobeadded;
                        tempmatrix[0][j] = value;
                    }
                    for(int j=1;j<=id;j++)
                    {
                        int value = tempmatrix[1][j];
                        value = value + valuetobeadded;
                        tempmatrix[1][j] = value;
                    }
                    if (pittoberemoved % (totallength - 3)==0 && (pittoberemoved==(totallength-3))) {

                        int value = tempmatrix[1][length - 1];
                        value = value + valuetobeadded + tempmatrix[0][id];
                        tempmatrix[0][id] = 0;
                        tempmatrix[1][id] = 0;
                        tempmatrix[1][length - 1] = value;
                    } else {
                        int remaning = pittoberemoved;
                        if(pittoberemoved>totallength-3) {
                            remaning= pittoberemoved % (totallength - 3);
                        }
                        //boolean flag = false;
                        while (remaning > 0) {
                            for (int j = id + 1; j <= length - 1; j++) {
                                if (tempmatrix[1][j] == 0 && remaning == 1 && j != length - 1) {
                                    int value = tempmatrix[1][tempmatrix[0].length-1];
                                    value=value+ remaning + tempmatrix[0][j];
                                    tempmatrix[1][length - 1] = value;
                                    tempmatrix[0][j] = 0;
                                    tempmatrix[1][j]=0;
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
                            if (remaning != 0) {
                                for (int k = 1; k < id; k++) {
                                    if (tempmatrix[1][k] == 0 && remaning == 1 && k != length - 1) {
                                        int value = tempmatrix[1][tempmatrix[0].length - 1];
                                        value = value + remaning + tempmatrix[0][k];
                                        tempmatrix[1][length - 1] = value;
                                        tempmatrix[0][k] = 0;
                                        tempmatrix[1][k] = 0;
                                        remaning = remaning - 1;
                                    }
                                        else {

                                        int value = tempmatrix[1][k];
                                        value = value + 1;
                                        tempmatrix[1][k] = value;
                                        remaning = remaning - 1;
                                    }
                                    if (remaning == 0) {
                                        break;
                                    }

                                }
                            }
                        }
                        if(level==1 && !flag)
                        {
                            finalmatrix=copyArray(tempmatrix);
                        }
                        if(originalplayer==1)
                        {
                            if(isEmpty(tempmatrix,1) || isEmpty(tempmatrix,0)) {
                                returnvalue = actualReturnValue(returnvalue,player,originalplayer,flag);
                                if(tasknum==2 || tasknum==3) {
                                    if(depth==0&&flag) {
                                        printNodes(returnvalue, player, id, level, depth, traverselogfile, alpha, Beta);
                                    }
                                    else if(depth ==0 && !flag)
                                    {

                                    }
                                    else
                                    {
                                        printNodes(returnvalue, player, id, level, depth, traverselogfile, alpha, Beta);
                                    }
                                }
                                if(level==1)
                                {
                                    finalmatrix=copyArray(tempmatrix);
                                    finalmatrix[0][0] = tempmatrix[0][0] + finaltotalSum(tempmatrix, 0);
                                    finalmatrix[1][finalmatrix[0].length - 1] = tempmatrix[1][tempmatrix[0].length - 1] + finaltotalSum(tempmatrix, 1);
                                    createZeroMatrix();
                                }
                                int firstvalue=tempmatrix[0][0] + finaltotalSum(tempmatrix, 0);
                                int secondvalue=tempmatrix[1][tempmatrix[0].length - 1] + finaltotalSum(tempmatrix, 1);

                                if(tasknum==2 || tasknum==3) {
                                    printNodes(evalFunction(secondvalue, firstvalue), player, id, level, depth,traverselogfile,alpha,Beta);
                                }
                                return evalFunction(secondvalue, firstvalue);
                            }
                        }
                        else
                        {
                            if(isEmpty(tempmatrix,0) || isEmpty(tempmatrix,1)) {
                                returnvalue = actualReturnValue(returnvalue,player,originalplayer,flag);
                                if(tasknum==2 || tasknum==3) {
                                    if(depth==0&&flag) {
                                        printNodes(returnvalue, player, id, level, depth, traverselogfile, alpha, Beta);
                                    }
                                    else if(depth ==0 && !flag)
                                    {

                                    }
                                    else
                                    {
                                        printNodes(returnvalue, player, id, level, depth, traverselogfile, alpha, Beta);
                                    }
                                }
                                if(level==1)
                                {
                                    finalmatrix=copyArray(tempmatrix);
                                    finalmatrix[0][0] = tempmatrix[0][0] + finaltotalSum(tempmatrix, 0);
                                    finalmatrix[1][finalmatrix[0].length - 1] = tempmatrix[1][tempmatrix[0].length - 1] + finaltotalSum(tempmatrix, 1);
                                    createZeroMatrix();
                                }
                                int firstvalue=tempmatrix[0][0] + finaltotalSum(tempmatrix, 0);
                                int secondvalue=tempmatrix[1][tempmatrix[0].length - 1] + finaltotalSum(tempmatrix, 1);

                                if(tasknum==2 || tasknum==3) {
                                    printNodes(evalFunction(firstvalue, secondvalue), player, id, level, depth,traverselogfile,alpha,Beta);
                                }
                                return evalFunction(firstvalue, secondvalue);
                            }
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
                    if (pittoberemoved % (totallength - 3)==0 && (pittoberemoved==(totallength-3))) {

                        int value = tempmatrix[0][0];
                        value = value + valuetobeadded + tempmatrix[1][id];
                        tempmatrix[0][id] = 0;
                        tempmatrix[1][id] = 0;
                        tempmatrix[0][0] = value;
                    }
                    else {
                        int remaning = pittoberemoved;
                        if(pittoberemoved>totallength-3) {
                            remaning= pittoberemoved % (totallength - 3);
                        }

                        while (remaning > 0) {
                            for (int j = id - 1; j >=0; j--) {
                                if (tempmatrix[0][j] == 0 && remaning == 1 && j != 0) {
                                    int value = tempmatrix[0][0];
                                     value=value+ remaning + tempmatrix[1][j];
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
                                        if (tempmatrix[0][k] == 0 && remaning == 1 && k != 0) {
                                            int value = tempmatrix[0][0];
                                            value = value + remaning + tempmatrix[1][k];
                                            tempmatrix[0][0] = value;
                                            tempmatrix[1][k] = 0;
                                            remaning = remaning - 1;
                                        }
                                        else {
                                            int value = tempmatrix[0][k];
                                            value = value + 1;
                                            tempmatrix[0][k] = value;
                                            remaning = remaning - 1;
                                        }
                                        if (remaning == 0) {
                                            break;
                                        }
                                    }
                                }

                            }

                        }
                        if(level==1 && !flag)
                        {
                            finalmatrix=copyArray(tempmatrix);
                        }
                        if(originalplayer==1)
                        {
                            if(isEmpty(tempmatrix,1) || isEmpty(tempmatrix,0)) {
                                returnvalue = actualReturnValue(returnvalue,player,originalplayer,flag);
                                if(tasknum==2 || tasknum==3) {
                                    if(depth==0&&flag) {
                                        printNodes(returnvalue, player, id, level, depth, traverselogfile, alpha, Beta);
                                    }
                                    else if(depth ==0 && !flag)
                                    {

                                    }
                                    else
                                    {
                                        printNodes(returnvalue, player, id, level, depth, traverselogfile, alpha, Beta);
                                    }
                                }
                                if(level==1)
                                {
                                    finalmatrix=copyArray(tempmatrix);
                                    finalmatrix[0][0] = tempmatrix[0][0] + finaltotalSum(tempmatrix, 0);
                                    finalmatrix[1][finalmatrix[0].length - 1] = tempmatrix[1][tempmatrix[0].length - 1] + finaltotalSum(tempmatrix, 1);
                                    createZeroMatrix();
                                }

                                int firstvalue=tempmatrix[0][0] + finaltotalSum(tempmatrix, 0);
                                int secondvalue=tempmatrix[1][tempmatrix[0].length - 1] + finaltotalSum(tempmatrix, 1);
                                if(tasknum==2 || tasknum==3) {
                                    printNodes(evalFunction(secondvalue, firstvalue), player, id, level, depth,traverselogfile,alpha,Beta);
                                }
                                return evalFunction(secondvalue, firstvalue);
                            }
                        }
                        else
                        {
                            if(isEmpty(tempmatrix,0) || isEmpty(tempmatrix,1)) {
                                returnvalue = actualReturnValue(returnvalue,player,originalplayer,flag);
                                if(tasknum==2 || tasknum==3) {
                                    if(depth==0&&flag) {
                                        printNodes(returnvalue, player, id, level, depth, traverselogfile, alpha, Beta);
                                    }
                                    else if(depth ==0 && !flag)
                                    {

                                    }
                                    else
                                    {
                                        printNodes(returnvalue, player, id, level, depth, traverselogfile, alpha, Beta);
                                    }
                                }
                                if(level==1)
                                {
                                    finalmatrix=copyArray(tempmatrix);
                                    finalmatrix[0][0] = tempmatrix[0][0] + finaltotalSum(tempmatrix, 0);
                                    finalmatrix[1][finalmatrix[0].length - 1] = tempmatrix[1][tempmatrix[0].length - 1] + finaltotalSum(tempmatrix, 1);
                                    createZeroMatrix();
                                }
                                int firstvalue=tempmatrix[0][0] + finaltotalSum(tempmatrix, 0);
                                int secondvalue=tempmatrix[1][tempmatrix[0].length - 1] + finaltotalSum(tempmatrix, 1);
                                if(tasknum==2 || tasknum==3) {
                                    printNodes(evalFunction(firstvalue, secondvalue), player, id, level, depth,traverselogfile,alpha,Beta);
                                }
                                return evalFunction(firstvalue, secondvalue);
                            }
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
                        if(isEmpty(tempmatrix,1) || isEmpty(tempmatrix,0))
                        {
                            int value=finaltotalSum(tempmatrix,0);

                            returnvalue = evalFunction(tempmatrix[1][tempmatrix[0].length - 1], tempmatrix[0][0]+value);

                        }
                        else {
                            returnvalue = evalFunction(tempmatrix[1][tempmatrix[0].length - 1], tempmatrix[0][0]);
                        }
                        if(tasknum==2 || tasknum==3) {
                            printNodes(returnvalue, player, id, level, depth,traverselogfile,alpha, Beta);
                        }

                        return returnvalue;
                    }
                    else
                    {
                        if(isEmpty(tempmatrix,0) || isEmpty(tempmatrix,1))
                        {
                            int value=finaltotalSum(tempmatrix,1);
                            returnvalue = evalFunction(tempmatrix[0][0], tempmatrix[1][tempmatrix[0].length - 1]+value);

                        }
                        else {
                            returnvalue = evalFunction(tempmatrix[0][0], tempmatrix[1][tempmatrix[0].length - 1]);
                        }
                        if(tasknum==2 || tasknum==3) {
                            printNodes(returnvalue, player, id, level, depth,traverselogfile,alpha,Beta);
                        }

                        return returnvalue;
                    }

                }

            returnvalue = actualReturnValue(returnvalue,player,originalplayer,flag);

            if(tasknum==2 || tasknum==3) {
                printNodes(returnvalue, player, id, level, depth,traverselogfile,alpha,Beta);
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
                    int val=max(calculateMinMax(tempmatrix,player,level,depth,truelevel+1,originalplayer,i,tasknum,traverselogfile,alpha,Beta),returnvalue);

                    if(val>returnvalue) {
                        if (level == 1 && flag)
                        {
                            returnstate=copyArray(finalmatrix);

                        }
                    }
                    returnvalue=val;
                    if(tasknum==3)
                    {
                        if(val>=Beta)
                        {
                            printNodes(returnvalue, player, id, level, depth,traverselogfile,alpha,Beta);
                            return val;
                        }
                        alpha=max(alpha,val);
                    }

                }
                else if(!flag && (player==originalplayer))
                {
                    if(isCellEmpty(tempmatrix,player^3,i))
                    {
                        continue;
                    }

                    returnvalue = min(calculateMinMax(tempmatrix, player ^ 3, level + 1, depth - 1, truelevel + 1, originalplayer, i, tasknum, traverselogfile,alpha,Beta), returnvalue);
                    if(tasknum==3)
                    {
                        if(returnvalue<=alpha)
                        {
                            printNodes(returnvalue, player, id, level, depth,traverselogfile,alpha,Beta);
                            return returnvalue;
                        }
                        Beta = min(Beta,returnvalue);
                    }


                }
                else if(flag && (player!=originalplayer))
                {
                    if(isCellEmpty(tempmatrix,player,i))
                    {
                        continue;
                    }
                    returnvalue=min(calculateMinMax(tempmatrix,player,level,depth,truelevel+1,originalplayer,i,tasknum,traverselogfile,alpha,Beta),returnvalue);
                    if(tasknum==3)
                    {
                        if(returnvalue<=alpha)
                        {
                            printNodes(returnvalue, player, id, level, depth,traverselogfile,alpha,Beta);
                            return returnvalue;
                        }
                        Beta = min(Beta,returnvalue);
                    }

                }
                else if(!flag && (player!=originalplayer))
                {
                    if(isCellEmpty(tempmatrix,player^3,i))
                    {
                        continue;
                    }

                    returnvalue = max(calculateMinMax(tempmatrix, player ^ 3, level + 1, depth - 1, truelevel + 1, originalplayer,i,tasknum,traverselogfile,alpha,Beta),returnvalue);
                    if(tasknum==3)
                    {
                        if(returnvalue>=Beta)
                        {
                            printNodes(returnvalue, player, id, level, depth,traverselogfile,alpha,Beta);
                            return returnvalue;
                        }
                        alpha=max(alpha,returnvalue);

                    }


                }

                if(tasknum==2 || tasknum==3) {
                    printNodes(returnvalue, player, id, level, depth,traverselogfile,alpha,Beta);
                }

            }
            if(returnstate[0]!=null) {
                finalmatrix = copyArray(returnstate);
            }
        }
        return returnvalue;
    }

    static void printNodes(Integer returnvalue,int player,Integer columnIndex,Integer level,int originalplayer,PrintWriter traverselogfile,Integer alpha,Integer Beta)
    {

        String value=returnvalue.toString();
        String alphaValue="";
        String BetaValue="";
        if(alpha!=null) {
          alphaValue  =alpha.toString();
           BetaValue =Beta.toString();
        }
        if(alpha!=null)
        {
            if(alpha==Integer.MAX_VALUE)
                alphaValue="Infinity";
            else if(alpha==Integer.MIN_VALUE)
                alphaValue="-Infinity";
        }
        if(Beta!=null)
        {
            if(Beta==Integer.MAX_VALUE)
                BetaValue="Infinity";
            else if(Beta==Integer.MIN_VALUE)
                BetaValue="-Infinity";
        }
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
            if(alpha!=null) {
                traverselogfile.print("\nB" + columnIndex.toString() + "," + level.toString() + "," + value+","+alphaValue+","+BetaValue);
            }
            else {
                traverselogfile.print("\nB" + columnIndex.toString() + "," + level.toString() + "," + value);
            }
        }
        else if(player==2)
        {
            if(alpha!=null) {
                traverselogfile.print("\nA" + columnIndex.toString() + "," + level.toString() + "," + value+","+alphaValue+","+BetaValue);
            }
            else {
                traverselogfile.print("\nA" + columnIndex.toString() + "," + level.toString() + "," + value);
            }
        }
        else
        {
            if(alpha!=null) {
                traverselogfile.print("\nroot," + "0" + "," + value+","+alphaValue+","+BetaValue);
            }
            else {
                traverselogfile.print("\nroot," + "0" + "," + value);
            }
        }

    }

    static int actualReturnValue(int returnvalue,int player,int originalplayer,boolean flag)
    {
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
        return returnvalue;
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

    static void createZeroMatrix()
    {
        for(int i = 0 ;i< finalmatrix.length;i++)
        {
            for(int j=1;j<finalmatrix[0].length-1;j++)
            {
                finalmatrix[i][j]=0;
            }
        }
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




