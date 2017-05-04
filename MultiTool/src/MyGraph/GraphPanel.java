package MyGraph;
//TODO aproksymacja
import java.awt.*;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;

import MyUtils.*;
import MyUtils.Point;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/* Dozwolone konstrukcje i znaki */
  // sin() cos() tan() ctg() sqr() lgn() abs()
  // x - zmienna
  // e - stała Eulera
  // p - stała PI
  // dozwolone cyfry 0-9 
  // kropka jako separator liczb zmiennoprzecinkowych
  // dozwolone operatory / * - + ();
/* koniec */

class GraphPanel extends JPanel implements MouseMotionListener, MouseListener{
	private static final long serialVersionUID = 1L;

	/* Flaga mówiąca o tym czy jesteśmy w trybie przeskalowania wykresu */
	private boolean rescalemode;
	/* Plik załadowany z wczytywania*/
	private File loadedfile;
	/* Lista rozwijalna wykresów*/
	private JComboBox<String> t2;
	private ComboBoxRenderer renderer;
	
	/* stałe używane przy generowaniu wykresów z pliku */
	public final int INTERPOLATION = 0; 
	public final int APPROXIMATION = 1; 
	public final int POINTS = 2; // generowanie punktów z pliku
	public final int NORMAL = 3; // tryb obliczania normalnego
	public final int NORMAL_MODE = 4; //łączenie linią z pliku
	
	
	private int fullWidth;
    private int fullHeight;
    private int halfWidth;
    private int halfHeight;
     /* pola obsługiwane przez funkcje parsujace */
		private String pattern;
	    private int iterator;
	    private ArrayList<Struct> onpList;
	    private ArrayList<Struct> list;
	    private Stack<Struct> stack;
	/* koniec */
    private String SX;
    private String SY;
    private DecimalFormat dF;
    private CoorSys system;
    private GraphsHolder graphs;
    
    private double LowIntegralLim;
    private double HighIntegralLim;
    
    private boolean excFlag; // flaga wystapienia błędu
    
    private double COORXPOS;
    private double COORYPOS;
    
    public boolean allowGraph; 
    public boolean allowCorSys;
    public boolean allowIntegral;
    public boolean skipparse; //porzuca parsowanie jeżeli z pliku
    public int scaleX;
    public int scaleY;
    public double delta;
    public double Period;
    
    public PanelRight panelRight;  
    public ButtonPanel buttonPanel;

	private GraphPoints ActualGraph; //aktualnie rozpatrywany graf do rysowania punktów po przeskalowaniu korzysta z niego przy rosowaniu wszystkich grafów
    
    
	
    
    public GraphPanel(){
    			this.setLayout(new FlowLayout(FlowLayout.LEFT));
    			t2 = new JComboBox<String>();
    			this.renderer = new ComboBoxRenderer(t2);
    			this.add(t2);
    			this.rescalemode = false;
				addMouseMotionListener(this);
				addMouseListener(this);
				setVisible(true);
				this.graphs = new GraphsHolder();
				this.allowGraph=false;
				this.allowCorSys=true;
				this.allowIntegral=false;
				this.skipparse = false;
				this.HighIntegralLim = 0.00;
				this.LowIntegralLim = 0.00;
				this.excFlag = false;
				this.scaleX=50;
				this.scaleY=50;
				this.delta =0.01;
				this.COORXPOS = 0;
				this.SX = null;
				this.COORYPOS = 0;
				this.SY = null;
				this.panelRight=null;
				/* inicjalizacja pól parsera */
				this.iterator=0;
				this.pattern=null;
				this.stack = new Stack<Struct>();
				this.list=new ArrayList<Struct>();
				this.onpList = new ArrayList<Struct>();
				this.system = CoorSys.CARTESIAN;
				/* koniec */
				DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
				otherSymbols.setDecimalSeparator('.');
				this.dF = new DecimalFormat("#.####",otherSymbols);
				
			}

   @Override
    protected void paintComponent(Graphics g){
	   System.out.println("paintComponent()");
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
            this.fullWidth = this.getWidth();
            this.fullHeight = this.getHeight();
            this.halfWidth = this.fullWidth/2;
            this.halfHeight = this.fullHeight/2;
		if(allowGraph){
			initGraph(g2d);
		}
		if(this.allowCorSys){
			initCoordinateSysytem(g2d);
		}
		if(this.excFlag){
			this.allowCorSys = true;
			this.excFlag = false;
		}
	}
   

   /**
    * Funkcja rysuje całkę
    * @param g2d
    * 			referencja na komponent graficzny
    */
   public void drawIntegral(Graphics2D g2d){
	   System.out.println("drawIntegral()");
	   	 GraphPoints gp = this.getSelectedGraph();
	     double l=LowIntegralLim;
	     double h=HighIntegralLim;
	     double x,y;
	     int height;
	     int xx,yy;
	     height= this.getHeight()/2;
	     g2d.setColor(gp.getColor());
	     //reysowanie całki interpolacyjnej
	     if(gp.getMethod()==this.INTERPOLATION){
	    	 double [] xs = this.fromPointListToArray(gp.getFromfilepoints(),"x");
	    	 double [] ys = this.fromPointListToArray(gp.getFromfilepoints(),"y");
	    	 
	    	 if(l<=h){
				 while(l<=h){
					  x=l;
					  y = this.lagrangeInterpolation(xs,ys,x);
					  y=halfHeight-(y*scaleY);
					  x=halfWidth +(x*scaleX);
					  yy =(int) Math.round(y);
					  xx =(int) Math.round(x);
					  g2d.drawLine(xx,
							  	   height,
								   xx,
								   yy
								  );
		
					 l=l+this.delta;   
			     }
		     }else{
				 while(l>h){
					  x=l;
					  y = this.lagrangeInterpolation(xs,ys,x);
					  y=halfHeight-(y*scaleY);
					  x=halfWidth +(x*scaleX);
					  yy =(int) Math.round(y);
					  xx =(int) Math.round(x);
					  g2d.drawLine(xx,
							  	   height,
								   xx,
								   yy
								  );
		
					 l=l-this.delta;   
			     }	 
		     }
	    	 
	     }else{
		     if(l<=h){
				 while(l<=h){
					  x=l;
					  y = getResult(l);
					  y=halfHeight-(y*scaleY);
					  x=halfWidth +(x*scaleX);
					  yy =(int) Math.round(y);
					  xx =(int) Math.round(x);
					  g2d.drawLine(xx,
							  	   height,
								   xx,
								   yy
								  );
		
					 l=l+this.delta;   
			     }
		     }else{
				 while(l>h){
					  x=l;
					  y = getResult(l);
					  y=halfHeight-(y*scaleY);
					  x=halfWidth +(x*scaleX);
					  yy =(int) Math.round(y);
					  xx =(int) Math.round(x);
					  g2d.drawLine(xx,
							  	   height,
								   xx,
								   yy
								  );
		
					 l=l-this.delta;   
			     }	 
		     }
	     }
	     this.allowIntegral = false;
   }

    /**
     * Funkcja rysująca układ współrzędnych
     * @param g2d
     * 			referencja na komponent graficzny
     */
	private void initCoordinateSysytem(Graphics2D g2d){
			System.out.println("initCoordinateSysytem()");
			g2d.setColor(Color.BLACK);
			g2d.drawLine(0,(this.getHeight()/2),this.fullWidth,(this.fullHeight/2));
			g2d.drawLine((this.getWidth()/2),0,(this.fullWidth/2),this.fullHeight);
        /* podziałka */
           /*pozioma*/
           int counter = this.fullWidth/this.scaleX;
           while (counter > 0){
			    g2d.drawLine(
					halfWidth+(scaleX*counter),
					(halfHeight+10),
					(halfWidth+(scaleX*counter)),
					(halfHeight-10)
				  );
				 g2d.drawLine(
					halfWidth-(scaleX*counter),
					(halfHeight+10),
					(halfWidth-(scaleX*counter)),
					(halfHeight-10)
				  );
			    counter=counter - 1;
			   } 
		   /* pionowa */	   
           counter = fullHeight/scaleY;
           while (counter > 0){
                g2d.drawLine(
                        (halfWidth -10),
                        (halfHeight+(scaleY*counter)),
                        (halfWidth +10),
                        (halfHeight+(scaleY*counter))
                            );
                g2d.drawLine(
                        (halfWidth -10),
                        (halfHeight-(scaleY*counter)),
                        (halfWidth +10),
                        (halfHeight-(scaleY*counter))
                            );
			    counter=counter - 1;
			   }
           counter = fullHeight/scaleY;        
	}

 /**
  * Ustawienie zmiennej prywatnej wzoru
  * @param pattern
  * 			wzór funkcji
  **/
 public void insertPattern(String pattern){
		System.out.println("insertPattern()");
	    this.pattern=new String(pattern);
 }
	   


 /**
  * Funkcja sprawdzająca poprawność wprowadzonych znaków we wzorze funkcji
  * @param pattern
  * 		wzór funkcji
  * @return
  * 		zwraca true jeżeli wzór zawiera poprawne znaki
  */
private boolean checkAlphabet(String pattern){
		System.out.println("checkAlphabet()");
		  char[] txt = pattern.toCharArray(); 
		  for(char c : txt){
			  switch(c){
				  /*matryca poprawnych znaków*/
				   case '1': case '2': case '3':
				   case '4': case '5': case '6': 
				   case '7': case '8': case '9':
				   case '0': case '^': case '/':
				   case '(': case ')': case '*':
				   case '+': case '-': case '.': 
				   case 'x': case 'e': case 'p':
				   case 's': case 'i': case 'n':
				   case 'c': case 'o': case 't':
				   case 'g': case 'a': case 'q':
				   case 'r': case 'l': case 'b':
					   
				   
				   break;
				   default: 
				    return false;
				  }
			  }
		  return true;
}


/**
 * Funkcja sprawdzająca poprawność konstrukcji całości wzoru 
 * @return
 * 		Zwraca true jeżeli wzór poprawnie zwalidowany
 */
private boolean parsePattern(){
	System.out.println("parsePattern()");
	 if (!checkAlphabet(this.pattern)){
		  return false;
		 }
     return true;
	}


/* funkcje parsujące */

/**
 * Metoda wyciąga liczby ze wzoru
 * @param pattern
 * 			wzór funkcji
 * @return
 * 			zwraca liczbę zmiennoprzecinkową, która wystapiła we wzorze
 */
private double getDouble(String pattern){
			char[] str = pattern.toCharArray();
			String doub = "";
			double result = 0.0;
			for(int i = this.iterator ;i<pattern.length();i++){
				 switch(str[i]){
					  case '0': case '1': case '2': case '3': case '4':
					  case '5': case '6': case '7': case '8': case '9':
					  case '.':
					   doub= doub + str[i];
					  break;	  
					  default :
					   result = Double.parseDouble(doub);
					   return result;
					 }
				this.iterator = i;
				}

			result = Double.parseDouble(doub);
			return result;
}


/**
 * Sprawdza czy podany znak jest operatorem matematycznym
 * @param x
 * 		znak do sprawdzenia
 * @return
 *      zwraca true jeżeli znak jest operatoprem
 */
private boolean isOper(char x){
	 switch(x){
		  case '+': case '-': case '*': case '/': case '^': case '(': case ')':
		   return true;
		 }
	  return false;
	}
	
/**
 * Sprawdza czy podany znak jest operatorem funkcyjnym
 * @param x
 * 		znak do sprawdzenia
 * @return
 *      zwraca true jeżeli znak jest operatorem funkcyjnym
 */
private boolean isFOper(char x){
	 switch(x){
		  case 's': case 'c': case 't': case 'q': case 'r': case  'l': case 'a':
		   return true;
		 }
	  return false;
	}	
	

/**
 * Sprawdza czy podany znak jest cyfrą
 * @param x
 * 		znak do sprawdzenia
 * @return
 *      zwraca true jeżeli znak jest cyfrą
 */
private boolean isNum(char x){
	 switch(x){
		  case '0': case '1': case '2': case '3': case '4': 
		  case '5': case '6': case '7': case '8': case '9':
		   return true;
		 }
	  return false;
	}
	
	

/**
 * Sprawdza czy podany znak jest zmienną
 * @param x
 * 		znak do sprawdzenia
 * @return
 *      zwraca true jeżeli znak jest zmienną
 */
private boolean isVar(char x){
  switch(x){
		  case 'x':
		   return true;
		 }
	  return false;
	}
	
	


/**
 * Sprawdza czy znak jest stałą
 * @param x
 * 		znak do sprawdzenia
 * @return
 *      zwraca true jeżeli znak jest stałą
 */
private boolean isConst(char x){
	switch(x){
		  case 'p': case 'e': 
		   return true;
		 }
	  return false;
	}
	
	

/**
 * Wydaje stałą na podstawie znaku
 * @param x
 * 		stała do zwrócenia
 * @return
 * 		zwraca liczbę kóra jest reprezentowana przez stałą
 */
private double getConst(char x){
	switch(x){
		  case 'p':
		   return Math.PI;
		  case 'e':
		   return Math.E;
		 }
	  return 0.00;
}



/**
 * Zwraca priorytet operatora
 * @param c
 * 		znak do sprawdzenia
 * @return
 *      zwraca liczbowy priorytet operatora
 */
private int getPriority(char c){
	switch(c){
		case '(' :
			return 0;
		case '+': case '-' :
		    return 1;
		case '*': case '/':
            return 2;
        case '^': 
            return 3;
        case 's': case 'c': case 't': case 'q': case  'r' : case 'l': case 'a':
			return 4;
      	}
    return 0;
}


/**
 * Pobranie funkcji
 * @return
 * 		zwraca znak funkcji operatora korzysta z głownego iteratora i wzoru
 */
private char getFunc(){
	 char[] arr =  this.pattern.toCharArray();
	 int i = this.iterator;
	 this.iterator =this.iterator + 2;
	 switch(arr[i]){
		 case 's' : 	  
			  if(arr[i+1]=='i'){
				return 's';  
			  }else{ 
			   return 'r';
			  }
		 case 'c':
			  if(arr[i+1]=='o'){
				return 'c';  
			  }else{ 
			   return 'q';
			  }
		 case 't':
		  return 't';
		 case 'l':
		  return 'l'; // logarytm naturalny
		 case 'a':
		  return 'a';
			 
	  }
	 return '#';
	}



/**
 * Zwraca wynik działania
 * @param x
 * 
 * @return
 */
private double getResult(double x){
	 Struct st,v1,v2;
	 int i,size;
	 size = this.onpList.size();
	 for(i=0;i<size;i++){
		  st = this.onpList.get(i);
		  switch(st.s){
			   case '#': this.stack.push(new Struct('x',st.n)) ; break;
			   case 'x': 
					st.n=x;
					this.stack.push(new Struct('x',st.n)) ;
				break;
			  case '+': 
				v1 = this.stack.pop();
				v2 = this.stack.pop();
				v1.n = v1.n + v2.n;
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case '-': 
				v1 = this.stack.pop();
				v2 = this.stack.pop();
				v1.n = v2.n - v1.n;
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case '*': 
				v1 = this.stack.pop();
				v2 = this.stack.pop();
				v1.n = v1.n * v2.n;
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case '/': 
				v1 = this.stack.pop();
				v2 = this.stack.pop();
				v1.n = v2.n / v1.n;
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case '^': //potęga
				v1 = this.stack.pop();
				v2 = this.stack.pop();
				v1.n = Math.pow(v2.n ,v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 's': //sinus
				v1 = this.stack.pop();
				v1.n = Math.sin(v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 'c': // cosinus
				v1 = this.stack.pop();
				v1.n = Math.cos(v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 't': // tangens
				v1 = this.stack.pop();
				v1.n = Math.tan(v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 'q': //cotangens
				v1 = this.stack.pop();
				v1.n =1.00/Math.tan(v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 'r': //pierwiastek
				v1 = this.stack.pop();
				v1.n =Math.sqrt(v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 'l': //logarytm naturalny
					v1 = this.stack.pop();
					v1.n =Math.log(v1.n);
					this.stack.push(new Struct('#',v1.n));
			   break;
			  case 'a': //wartosc absolutna
					v1 = this.stack.pop();
					v1.n =Math.abs(v1.n);
					this.stack.push(new Struct('#',v1.n));
			   break;
			  case '=': 
				v1 = this.stack.pop();
				return v1.n;
			  }

		 }
	return 0.00;
}
 

/**
 * Metoda ładuje łańcuch znaków do listy wzoru operuje na polach prywatnych iterator , list oraz używa pola pattern
 */
private void toList(){
		System.out.println("toList()");
		   this.list.clear();
		   this.iterator = 0;
		   char[] str = this.pattern.toCharArray();
		   int len = str.length;
		   
		  while(this.iterator<len){
			
			if(isNum(str[this.iterator])){									//jeżeli cyfra
				
			   this.list.add(new Struct('#',getDouble(this.pattern)));
			   if((len>1) && (this.iterator<len-1)){
				  if(isVar(str[this.iterator+1])){
					 this.list.add(new Struct('*',0.00));
				  }
			   }
			   	   
			}else if(isOper(str[this.iterator])){							//jezeli operator
				if((this.iterator == 0) && (str[this.iterator] == '-')){
					if((isNum(str[this.iterator+1])) || (isVar(str[this.iterator+1])) || (str[this.iterator+1] == '(') || (isFOper(str[this.iterator+1]))){
						this.list.add(new Struct('#',-1.00));
						this.list.add(new Struct('*',0.00));
					}
				}else if(str[this.iterator] == '('){
				  this.list.add(new Struct(str[this.iterator],0.00));
				  if(str[this.iterator+1]=='-'){
							this.iterator = this.iterator + 1;
							this.list.add(new Struct('#',-1.00));
							this.list.add(new Struct('*',0.00));
					}
				}else{
					this.list.add(new Struct(str[this.iterator],0.00));
				}

			}else if(isVar(str[this.iterator])){							//jezeli zmienna
				
				this.list.add(new Struct(str[this.iterator],0.00));					
				
			}else if(isFOper(str[this.iterator])){							//jeżeli funkcja
				
				this.list.add(new Struct(this.getFunc(),0.00));		
												
			}else if(isConst(str[this.iterator])){							// jeżeli stała
				
				this.list.add(new Struct('#',this.getConst(str[this.iterator])));
				
			}
			this.iterator = this.iterator +1;
	      }
		  this.list.add(new Struct('=',0.00));
}
  
    

/**
 * Metoda przekształca listę z wyrażeniem w zwykłej postaci na listę w postaci onp
 */
private void toOnpList(){
	System.out.println("toOnpList()");
		this.onpList.clear();
	   /* kod przekształcający */
	   int i;
	   Struct st;
	   for(i=0;i<this.list.size();i++){
		    st = this.list.get(i);
		    if(st.s=='='){
				while(!this.stack.empty()){
					 this.onpList.add(this.stack.pop());
					}
			  break;
			}else{
			   if((st.s =='#') || (isVar(st.s)) ){
				 this.onpList.add(st);  
			   }else if(st.s =='('){
				 this.stack.push(st); 
			   }else if(st.s ==')'){
				   while(this.stack.peek().s!='('){
					    this.onpList.add(this.stack.pop());
					   }
				   this.stack.pop();
		       }else if(isOper(st.s) || isFOper(st.s)){
				  while((!this.stack.empty()) && ((getPriority(st.s) < getPriority(this.stack.peek().s))) ){
					 this.onpList.add(this.stack.pop()); 
				  } 
				  this.stack.push(st);
			   }
		    }
	   }
	   this.onpList.add(new Struct('=',0.00));
}


/**
 * Metoda oczyszczająca listy i stos 
 */
private void clearAbstractElements(){
	System.out.println("clearAbstractElements()");
	while(!this.stack.empty()){
			 this.stack.pop();
	 }
	this.list.clear();
	this.onpList.clear();
    this.iterator = 0;
}



/**
 *Sprawdzenie poprawności grafu do narysowania
 * @param g2d
 * 			Komponent graficzny
 */
public void initGraph(Graphics2D g2d){
	System.out.println("initGraph()");
		if(this.isRescalemode()){this.drawAllGraphs(g2d);return;} //TODO tesotowe
	   if(this.skipparse){this.skipparse = false;this.drawAllGraphs(g2d);return;}  //TODO tu jestem
	   if(!t2.isVisible()){this.t2.setVisible(true);this.allowGraph = true;}
	   if(this.isPatternOnList() && !this.isRescalemode()){
		   System.out.println("był na liście");
		   this.drawAllGraphs(g2d);
			if(this.allowIntegral ){
				//TODO usunąć z tego warunku aproksymacje po zrobieniu całki
				if(this.getSelectedGraph().getMethod()==this.POINTS || this.getSelectedGraph().getMethod()==this.NORMAL_MODE || this.getSelectedGraph().getMethod()==this.APPROXIMATION){return;}
				try{
				 drawIntegral(g2d);
				 this.allowIntegral = false;
				}catch(Exception x){
					//TODO tu musze obsłużyć wyjątek :)
				}
			}
		   return;
	  }
		  if(this.pattern!=null && this.pattern.length()!=0){
			if(parsePattern()){
				if(this.allowGraph){
					clearAbstractElements();
					toList();
					toOnpList();
					if(this.allowIntegral){
						try{
						if(this.getSelectedGraph().getMethod()==this.POINTS || this.getSelectedGraph().getMethod()==this.NORMAL_MODE || this.getSelectedGraph().getMethod()==this.APPROXIMATION){return;}
						 drawIntegral(g2d);
						}catch(Exception x){
							//TODO tu musze obsłużyć wyjątek :)
							//x.printStackTrace();
						}
					}
					try{
						this.allowIntegral = false;
						this.drawGraph(g2d);
						g2d.setColor(Color.BLACK);
					 }catch(Exception exc){
						this.excFlag = true;
						this.allowCorSys = false;				  
					    g2d.setColor(Color.RED);
						g2d.drawString("Błąd",30,110);
						g2d.drawString("Program nie był w stanie",30,130);			// tu jestemmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
						g2d.drawString("wygenerować wykresu",30,150);		
						this.t2.setVisible(false);
					 }
				}
			}else{ 
				 this.allowCorSys = false;
				 g2d.setColor(Color.RED);
				 g2d.drawString("Wprowadzono niedozwolone znaki",1,110);
				 g2d.drawString("lub nieprawidłowa konstrukcja",1,130);
				 g2d.drawString("wyrażenia! ",1,150);
				 this.t2.setVisible(false);
		  }
	   }else if(this.graphs.getGraphlist().size()>0){
		   this.drawAllGraphs(g2d);
			if(this.allowIntegral){
				try{
				if(this.getSelectedGraph().getMethod()==this.POINTS || this.getSelectedGraph().getMethod()==this.NORMAL_MODE || this.getSelectedGraph().getMethod()==this.APPROXIMATION){return;}
				 drawIntegral(g2d);
				}catch(Exception x){
					//TODO tu musze obsłużyć wyjątek :)
				}
			}
	   }
		
}
		 
/**
 * Metoda generująca wykres w zalezności od warunków wejściowych
 * @param g2d
 * 				referencja na komponent graficzny
 */
private void drawGraph(Graphics2D g2d){
	System.out.println("drawGraph()");
			if(!this.isPatternOnList()){
			 this.generatePoints(g2d);
			}
			this.drawAllGraphs(g2d);
}

/**
 * Metoda generująca listy punktów dla wzorów. wykresy są natępnie trzymane w Instancji klasy GraphsHolder
 * @param g2d
 * 			referencja na komponent graficzny
 */

private void generatePoints(Graphics2D g2d){
	System.out.println("generatePoints()");
	GraphPoints gp = this.ActualGraph;
	if(gp==null){
		gp=new GraphPoints(this.pattern,new Color((int)(Math.random() * 0x1000000)),this.NORMAL);
		this.t2.addItem(gp.getPattern());
		this.graphs.getGraphlist().add(gp);
	}else{
		this.pattern = gp.getPattern();
		this.toList();
		this.toOnpList();
	}
	this.t2.setSelectedItem(gp.getPattern());
	int prevX,prevY,nextX,nextY,tmpX,tmpY;
	double x , y,r, fi, counter;
	y = 0.0000;
	 try{
	  delta = Double.parseDouble(panelRight.DELTA.getText());
	 }catch(Exception exc){
	  delta = 0.001;
	  panelRight.DELTA.setText("0.001");
	 }
       if((delta>0.8)||(delta<0.0001)){
		 delta = 0.001;
		 panelRight.DELTA.setText("0.001");
        }
       
	 counter= fullWidth/delta;    
     x=-halfWidth;
     prevX = -2;
     prevY = -2;		
	 for(int i = 0 ; i<=counter ; i++){
          y=getResult(x);
          y =halfHeight-(y*scaleY);
          nextY = (int) Math.round(y);
          y = halfWidth +(x*scaleX);
          nextX =(int) Math.round(y);  
   	      tmpX = nextX;
   	      tmpY = nextY;
         if((prevY > nextY)&&((prevY-nextY)>200)){
        	   nextX = prevX;
        	   nextY = prevY;
        	   }
         if((prevY < nextY)&&((nextY-prevY)>200)){
      	   prevX = nextX;
      	   prevY = nextY;
      	   }
		 gp.getPoints().add(new Point(nextX,nextY));
		 prevX = tmpX;
		 prevY=  tmpY; 
		 x=x+delta;    
     }
	 // obliczenia biegunowe
    try{
		 Period = Double.parseDouble(panelRight.MAXFI.getText());
		 }catch(Exception exc){
		  Period = 12;
		  panelRight.MAXFI.setText("12");
		}
        if((Period>100)||(Period<0)){
			Period = 12;
			panelRight.MAXFI.setText("12");	
        }
	counter = (Period*Math.PI)/delta;
	g2d.setColor(Color.ORANGE);
  	prevX = 0;
	prevY = 0;
	fi = delta;	
      for(int i=0;i<=counter;i++){
          r=getResult(fi); 
          y = r* Math.sin(fi);
          x = r * Math.cos(fi);
          y =halfHeight-(y*scaleY);
          nextY = (int) Math.round(y);
          y = halfWidth +(x*scaleX);
          nextX =(int) Math.round(y);  
          if(i==0){
          	prevX = nextX;
        	prevY = nextY;
          }         
         gp.getPolarpoints().add(new Point(nextX,nextY));
		 prevX = nextX;
		 prevY=  nextY; 
		 fi=fi+delta;    
	  }
}

/**
 * Metoda rysuje grafy na komponencie graficznym na podstawie punktów z list grafów
 * @param g2d
 * 			referencja na komponent graficzny
 */

public void drawAllGraphs(Graphics2D g2d){
	System.out.println("drawAllGrpahs()");
	//modyfikacja kolorów listy rozwijalnej
		for(GraphPoints points : this.graphs.getGraphlist()){
			g2d.setColor(points.getColor());
			if(points.getMethod() == this.NORMAL){
				if(this.isRescalemode()){
					points.clearAll();
					this.ActualGraph = points;
					System.out.println("Tryb przeskalowania");
					this.t2.setSelectedItem(this.ActualGraph);
					this.pattern = this.ActualGraph.getPattern();
					this.generatePoints(g2d);
				}
				this.drawPoints(g2d,points);
			}else if(points.getMethod()==this.POINTS){
				System.out.println("generuje punkty");
				this.makePoints(g2d,points);
			}else if(points.getMethod()==this.INTERPOLATION){
				this.makeInterpolation(g2d,points);
			}else if(points.getMethod()==this.APPROXIMATION){
				this.makeApproximation(g2d,points);
			}else if(points.getMethod() == this.NORMAL_MODE){
				System.out.println("tryb lini łączenie");
				this.makeNormalLines(g2d,points);
			}
			this.ActualGraph = null;
		}
		this.setRescalemode(false);
}

/**
 * Metoda interpolująca
 * @param g2d
 * 			komponentraficzny
 * @param points2
 * 			wykres
 */
private void makeInterpolation(Graphics2D g2d, GraphPoints points2) {
	System.out.println("interpolacjaaaaa");
	if(!points2.isAlreadyCalculated()){
		points2.setAlreadyCalculated(true);
		double []xs = this.fromPointListToArray(points2.getPoints(),"x");
		double []ys = this.fromPointListToArray(points2.getPoints(),"y");
        double max = this.max(xs);
        double min = this.min(xs);
        ArrayList<Point> p = new ArrayList<Point>();
        while(min<max){
        	p.add(new Point(min,this.lagrangeInterpolation(xs, ys,min)));
        	min = min + this.delta;
        }		
        points2.setFromfilepoints(points2.getPoints());
        points2.setPoints(p);
	}
	
	this.drawPoints(g2d, points2);
}

/**
 * Generuje tablicę współrzednych z listy punktów 
 * @param points2
 * 			Lista punktów			
 * @param string
 * 			współrzędna do wyciągnięcia jeżeli ustawiona na "x" to zwracamy tablice współrzednych x, analogicznie "y"
 * @return
 * 			Zwraca tablicę współrzędnych
 */
private double[] fromPointListToArray(ArrayList<Point> points2, String string) {
	// TODO Auto-generated method stub
	if(points2 == null)return null;
	double [] tab =new double[points2.size()];
	int index = 0;
	if(string.equals("x")){
		for(Point pp:points2){
			tab[index] = pp.getX();
			index = index +1;
		}
	}else{
		for(Point pp:points2){
			tab[index] = pp.getY();
			index = index +1;
		}
	}
	return tab;
}

/**
 * Metoda szukania minimum
 * @param tab
 * 			tablica do przeszukania
 * @return
 * 		Zwracanajmniejszą liczbę
 */
private double min(double [] tab){
	double min = 0;
	for(double m:tab){
		if(m<min){min = m;}
	}
	return min;
}

/**
 * Metoda szukania maksimum
 * @param tab
 * 			tablica do przeszukania
 * @return
 * 		Zwraca największą liczbę
 */
private double max(double [] tab){
	double max = 0;
	for(double m:tab){
		if(m>max){max = m;}
	}
	return max;
}

/**
 * Metoda interpolująca według zadanych punktów
 * @param xs
 * 			tablica współrzednych x
 * @param ys
 * 			tablica współrzędnych y
 * @param x
 * 			punkt x do policzenia
 * @return
 */
public double lagrangeInterpolation(double[] xs, double[] ys, double x ){
double t;
double y = 0.0;
 
for(int k = 0; k< xs.length; k++){
	t = 1.0;
	for(int j = 0; j < xs.length ; j++){
		if(j != k ){
		t=t*((x-xs[j])/(xs[k]-xs[j])); 
		}
	}
	y += t*ys[k];
}
return y;
}



/**
 * Metoda aproksymująca
 * @param g2d
 * 			komponentraficzny
 * @param points2
 * 			wykres
 */
private void makeApproximation(Graphics2D g2d, GraphPoints points2) {
	//TODO aproksymacja
	
	
}

/**
 * Rysuje graf według punktów z listy według określonych reguł
 * @param g2d
 * 		komponent graficzny
 * @param p
 * 		lista punktów
 * 
 */
private void drawPoints(Graphics2D g2d,GraphPoints points2){
    g2d.setColor(points2.getColor());
	int index = 0;
	ArrayList<Point>p= null;
	if(this.system.equals(CoorSys.POLAR) && points2.getMethod()==this.NORMAL){
		if(this.skipEquation(points2)){return;}
		p = points2.getPolarpoints();
	}else{
	    p = points2.getPoints();
	}
	if(points2.getMethod()==this.NORMAL){
		System.out.println("rysuje punkty "+p.size()+" "+p.get(300).getY());
		for(index = 0; index<p.size()-1;index=index+1){
			g2d.drawLine(
							(int)p.get(index).getX(),
							(int)p.get(index).getY(),
							(int)p.get(index+1).getX(),
							(int)p.get(index+1).getY()
						);
		}
	}else{
	    int x=0;
	    int y=0;
	    int x2=0;
	    int y2=0;
	    int i = 0;
			for(i=0;i<points2.getPoints().size()-1;i++){
				x = (int)(this.halfWidth+(points2.getPoints().get(i).getX()*scaleX));
				y = (int)(this.halfHeight-(points2.getPoints().get(i).getY()*scaleY));
				x2 = (int)(this.halfWidth+(points2.getPoints().get(i+1).getX()*scaleX));
			    y2 = (int)(this.halfHeight-(points2.getPoints().get(i+1).getY()*scaleY));
				g2d.drawLine(x,y,x2,y2);
			}
	}
}


/**
 * Metoda tworzy zbiór punktów na podstawie znalezionego wykresu
 * @param g2d
 * 			komponentraficzny
 * @param gp
 * 			wykres
 */
private void makePoints(Graphics2D g2d,GraphPoints gp) {
	System.out.println("makePoints()");
    g2d.setColor(gp.getColor());
    int x=0;
    int y=0;
	for(Point p:gp.getPoints()){
		x = (int)(this.halfWidth+(p.getX()*scaleX));
		y = (int)(this.halfHeight-(p.getY()*scaleY));
		g2d.fillRect(x,y,3,3);
	}
}

/**
 * Metoda łącząca punkty podane
 * @param g2d
 * 			komponentraficzny
 * @param points2
 * 			wykres
 */
private void makeNormalLines(Graphics2D g2d, GraphPoints points2){
	System.out.println("makeNormalLines()");
	this.makePoints(g2d,points2);
    g2d.setColor(points2.getColor());
    int x=0;
    int y=0;
    int x2=0;
    int y2=0;
    int i = 0;
 //   points2.getPoints().sort((Comparator<? super Point>) points2.getPoints());
	for(i=0;i<points2.getPoints().size()-2;i++){
		x = (int)(this.halfWidth+(points2.getPoints().get(i).getX()*scaleX));
		y = (int)(this.halfHeight-(points2.getPoints().get(i).getY()*scaleY));
		x2 = (int)(this.halfWidth+(points2.getPoints().get(i+1).getX()*scaleX));
	    y2 = (int)(this.halfHeight-(points2.getPoints().get(i+1).getY()*scaleY));
		g2d.drawLine(x,y,x2,y2);
	}
}

/**
 * Metoda ma za zadanie wyszukiwanie funkcji zabronionych we wzorach funkcji
 * @param p
 * 		   graf do sprawdzenia
 * @return
 * 			zwraca true jeżeli znaleziono funkcje zabronioną
 */
private boolean skipEquation(GraphPoints p) {
	System.out.println("skipEquation()");
    int index = 0;;
    for(String s: Config.BLACK_LIST){
    	index = p.getPattern().indexOf(s);
    	if(index != -1){
        	return true;
    	}
    }
    return false;
}
/**
 * Czyszczenie danych
 */
public void clearAll(){
	System.out.println("clearAll()");
	this.pattern = "";
	this.onpList.clear();
	this.list.clear();
	this.graphs.clearAll();
	this.t2.removeAllItems();
	this.renderer = new ComboBoxRenderer(t2);
}
/**
 * Metoda zmienia system współrzędnych
 */
public void changeSystem(){
	System.out.println("changeSysytem()");
	if(this.system.equals(CoorSys.POLAR)){
	 this.system = CoorSys.CARTESIAN;	
	 this.panelRight.SystemNameLabel.setText("Cartesian");
	 this.buttonPanel.Label1.setText("f(x)=");
	 this.panelRight.TLabel.setVisible(false);
	 this.panelRight.PiLabel.setVisible(false);
	 this.panelRight.MAXFI.setVisible(false);
	 this.panelRight.FakeLabel.setVisible(true);
	 this.panelRight.IntegralButton.setEnabled(true);
	 this.panelRight.LowIntegralLim.setEditable(true);
	 this.panelRight.HighIntegralLim.setEditable(true);
	 this.panelRight.xlabel.setText("X :");
	 this.panelRight.ylabel.setText("Y :");
	}else{
	 this.system = CoorSys.POLAR;
	 this.panelRight.SystemNameLabel.setText("Polar");
	 this.buttonPanel.Label1.setText("r  = ");
	 this.panelRight.TLabel.setVisible(true);
	 this.panelRight.PiLabel.setVisible(true);
	 this.panelRight.MAXFI.setVisible(true);
	 this.panelRight.FakeLabel.setVisible(false);
	 this.panelRight.IntegralButton.setEnabled(false);
	 this.panelRight.LowIntegralLim.setEditable(false);
	 this.panelRight.HighIntegralLim.setEditable(false);
	 this.panelRight.xlabel.setText("R :");
	 String str = "\u03D5";
	 this.panelRight.ylabel.setText(str+" :");
	}	
}

/**
 * Metoda obliczająca całkę numeryczną metodą prostokątów z nadmiarem
 * @param l
 * 			dolna granica całkowania
 * @param h
 * 			górna granica całkowania
 * @return
 * 			zwraca wynik w postaci łańcucha znaków
 */
public String calcIntegral(double l, double h){
	System.out.println("calcIntegral()");
	double x, delta;
	delta = 0.00001;
	double result = 0.000;      
  	 GraphPoints gp = this.getSelectedGraph();
  	 if(gp.getMethod()== this.INTERPOLATION){        //całkowanie wykresu interpolowanego
  		 double [] xs = this.fromPointListToArray(gp.getFromfilepoints(),"x");
  		 double [] ys = this.fromPointListToArray(gp.getFromfilepoints(),"y");
  		 
  		 double max = this.max(xs);
  		 double min = this.min(xs);
  		System.out.println("max = "+ max);
  		System.out.println("min = "+ min);
  		if((l>min) && (h<max)){
  			System.out.println("wesłooooo");
  			x = l;
  			if(l<=h){
  			 	while(x<h){
  			      result = result + (this.lagrangeInterpolation(xs,ys,x)*delta);
  			 	  x = x+delta;   	 
  			 	 }
  			}else{
  				 while(x>h){
  					  result = result - (this.lagrangeInterpolation(xs,ys,x)*delta);
  				  x = x-delta;   
  				 }		
  			}
  		    this.LowIntegralLim = l;
  		    this.HighIntegralLim = h;
  		    this.allowIntegral = true;
  		    this.repaint();
  		}else{
  			//TODO powiadominie że granice całkowania nie mieszczą sie w interpolowanym złe
  		}
  	 }else{
	  	 //TODO wstawka całka tylko dla trybu normalnego
	  	 if(gp.getMethod()!=3){return " ";}
	  	 this.insertPattern(gp.getPattern());
	  	 this.toList();
	  	 this.toOnpList();
		x = l;
		if(l<=h){
		 	while(x<h){
		      result = result + (getResult(x)*delta);
		 	  x = x+delta;   	 
		 	 }
		}else{
			 while(x>h){
				  result = result - (getResult(x)*delta);
			  x = x-delta;   
			 }		
		}
	    this.LowIntegralLim = l;
	    this.HighIntegralLim = h;
	    this.allowIntegral = true;
	    this.repaint();
  	 }
	return this.dF.format(result);
}

/* Funkcje słuchaczy zdarzeń */     
	  @Override 
	  public void mouseMoved(MouseEvent e)
	      {
		     double x = 0.00;
			 /* pobranie i obróbka współrzędnych */ 
			  if(system.equals(CoorSys.CARTESIAN)){
				 if(this.panelRight!= null){	  
					 this.COORXPOS=e.getX();
					 this.COORYPOS=e.getY();
					 this.COORXPOS= (double)(this.COORXPOS - this.halfWidth)/this.scaleX;
					 this.SX = this.dF.format(this.COORXPOS);
					 this.panelRight.COORX.setText(SX);	 	
					 this.COORYPOS= (double)(this.halfHeight - this.COORYPOS)/this.scaleY;
					 this.SY =this.dF.format(this.COORYPOS);
					 this.panelRight.COORY.setText(SY);
				}
			  }else{
				if(this.panelRight!= null){	  
				    this.COORXPOS=e.getX();
					this.COORYPOS=e.getY();
					this.COORXPOS= (double)(this.COORXPOS - this.halfWidth)/this.scaleX;	
					this.COORYPOS= (double)(this.halfHeight - this.COORYPOS)/this.scaleY;
					x = COORXPOS;
					this.COORXPOS = Math.sqrt(Math.pow(COORXPOS,2)+Math.pow(COORYPOS,2)); // x -> r
					if((x>0) && (COORYPOS>=0)){
						this.COORYPOS =Math.atan(COORYPOS/x) ;// Y -> fi	
					}else if((x>0) && (COORYPOS<0)){
						this.COORYPOS =Math.atan(COORYPOS/x)+(2*Math.PI);
					}else if(x<0){
						this.COORYPOS =Math.atan(COORYPOS/x)+Math.PI;
					}else if((x == 0.00) && (COORYPOS>0)){
						this.COORYPOS = Math.PI/2;
					}else if ((x == 0.00) && (COORYPOS<0)){
						COORYPOS = (3*Math.PI)/2;
					}
					 
					this.SX = this.dF.format(this.COORXPOS);
					this.panelRight.COORX.setText(SX);	 
					this.SY =this.dF.format(this.COORYPOS);
					this.panelRight.COORY.setText(SY);
				}  
			  }
		  }
/**
 * Metoda sprawdza czy wzór funkcji jest na liście
 * @param pattern2 
 * @return
 * 		Zwraca true jeżeli tak
 */
private boolean isPatternOnList(){
	for(GraphPoints g: graphs.getGraphlist()){
		if(g.getPattern().equals(this.pattern)){
			return true;
		}
	}
	return false;
}

/**
 * 
 * @return
 * 		Zwraca graf zaznaczony w polu wyboru
 */
private GraphPoints getSelectedGraph(){
	for(GraphPoints gp : this.graphs.getGraphlist()){
		if(gp.getPattern().equals(this.t2.getSelectedItem())){
			return gp;
		}
	}
	return null;
}
	    
	  @Override
	  public void mouseDragged(MouseEvent e){
		  
		  }
	 /* metoda wywoływana gdy kursor opuszcza nasłuchiwany obiekt*/
	  @Override
	  public void mouseExited(MouseEvent e){
		    if(this.panelRight!= null){
			 this.panelRight.COORX.setText("0.00");
			 this.panelRight.COORY.setText("0.00");
		    }
	  }
	//metoda wywoływana, gdy następuje kliknięcie, czyli wciśnięcie i zwolnienie przycisku myszy, ale uwaga, obie te operacje muszą zajść w jednym położeniu.
	@Override
	public void mouseClicked(MouseEvent event){}
	 
	//metoda wywoływana, gdy zostaje wciśnięty przycisk myszy
	@Override
	public void mousePressed(MouseEvent event){}
	 
	//metoda wywoływana, gdy następuje zwolnienie przycisku myszy
	@Override
	public void mouseReleased(MouseEvent event){}
	 
	//metoda wywoływana, gdy kursor pojawia się w obszarze nasłuchującym na zdarzenia, na przykład panelu
	@Override
	public void mouseEntered(MouseEvent e){

		
		}

	/**
	 * Metoda generująca wykres z pliku
	 * @param selectedFile
	 */
	public void generateFromFile(File selectedFile, int method) {
		System.out.println("generateFromFile()");
		this.loadedfile = selectedFile;
	    int index = 0;;
	    for(String s: Config.ALLOWED_EXTENSIONS){
	    	index = this.loadedfile.getName().indexOf(s);
	    	if(index != -1){
	    		if(this.loadFileToPoints(method)){
	    			this.allowGraph = true;
	    			this.skipparse = true;
	    			this.repaint();   //TODO jakby się zapętliło to tu jest przyczyna
	    		}
	    		return;
	    	}
	    }
	}
	/**
	 * Metoda ładuje dane z pliku csv do listy wykresów jako punkty
	 */
	private boolean loadFileToPoints(int method) {
		System.out.println("loadFilePoints()");
		double x= 0;
		double y = 0;
		int delimiterindex = 0;
		BufferedReader in;
		String operationname = "points";
		
		switch(method){
			case APPROXIMATION:
				operationname = "approximation";
			break;
			case INTERPOLATION :
				operationname = "interpolation";
			break;
			case NORMAL_MODE:
				operationname = "lines";
			break;
		}
	    GraphPoints gp = new GraphPoints(this.loadedfile.getName().toString()+"-"+operationname,new Color((int)(Math.random() * 0x1000000)),method);
	   //TODO przypisanie pliku do grafu
	    gp.setSource(this.loadedfile);
	    
	    this.graphs.getGraphlist().add(gp);
		try {
			in = new BufferedReader(
			        new InputStreamReader(new FileInputStream(this.loadedfile)));
		    String r;
		    this.t2.addItem(gp.getPattern()); //TODO lista rozwijalna
		    this.t2.setSelectedItem(gp.getPattern());
			while((r = in.readLine()) != null) {
				delimiterindex = r.indexOf(Config.CSV_SEPARATOR); //
				r=r.replaceAll(",",".");
				try{
					x = Double.parseDouble(r.subSequence(0,delimiterindex).toString());
					y = Double.parseDouble(r.subSequence(delimiterindex+1,r.length()-1).toString());
					gp.getPoints().add(new Point(x,y));
				}catch(Exception e){
					in.close();
					this.graphs.getGraphlist().remove(gp);
					this.pattern = "";
					gp.clearAll();
					e.printStackTrace();
					return false;
				}
			}
			in.close();
		} catch (IOException e) {
			this.graphs.getGraphlist().remove(gp);
			this.pattern = "";
			gp.clearAll();
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isRescalemode() {
		return rescalemode;
	}

	public void setRescalemode(boolean rescalemode) {
		this.rescalemode = rescalemode;
	}
}