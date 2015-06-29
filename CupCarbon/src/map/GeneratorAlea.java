package map;

import java.util.Random;


public class GeneratorAlea {
	public static int compteur=-1;
	public static int tab[]=new int[100000000];
	static boolean ini=false;
	
	public void RandomFibo(int x1,int x2,int x3,int m){
		for(int i=0;i<tab.length;i++){
			tab[i]=((x2+x3))%m;
			x3=x2;
			x2=x1;
			x1=tab[i];
		}
		
	}
	public int Random(int x1,int x2,int x3,int m){
		if(ini==false){
		RandomFibo(x1,x2,x3,m);
		System.out.println("premiere ité");
		ini=true;
		}
		compteur++;
		return tab[compteur];
		}
	public static void main(String args[])
	{
		Random r=new Random(231);
		for(int i=0;i<1000;i++)
		System.out.println(Math.abs(r.nextInt()%50));
	}
}
