package flashcardgenerator;

public class Priority implements Comparable {

    private int news;
    private int ichi;
    private int spec;
    private int gai;
    private int nf;

    public Priority(String prio) {
	this();
	this.addPriority(prio);
    }

    public Priority() {
	this.news = 99;
	this.ichi = 99;
	this.spec = 99;
	this.gai = 99;
	this.nf = 99;
    }

    @Override
    public int compareTo(Object arg0) {
	// TODO Auto-generated method stub
	if (!(arg0 instanceof Priority)) {
	    throw new ClassCastException("benis");
	}

	Priority otherPriority = (Priority) arg0;

	int retCompare = 0;

	if (retCompare == 0) {
	    retCompare = ((Integer) this.ichi).compareTo(otherPriority.ichi);
	}
	if (retCompare == 0) {
	    retCompare = ((Integer) this.nf).compareTo(otherPriority.nf);
	}
	if (retCompare == 0) {
	    retCompare = ((Integer) this.gai).compareTo(otherPriority.gai);
	}
	System.out.println(retCompare);
	return retCompare;

    }

    public void addPriority(String prio) {

	if (prio.startsWith("news")) {
	    this.news = Integer.parseInt(prio.split("news")[1]);
	}
	if (prio.startsWith("ichi")) {
	    this.ichi = Integer.parseInt(prio.split("ichi")[1]);
	}
	if (prio.startsWith("spec")) {
	    this.spec = Integer.parseInt(prio.split("spec")[1]);
	}
	if (prio.startsWith("gai")) {
	    this.gai = Integer.parseInt(prio.split("gai")[1]);
	}
	if (prio.startsWith("nf")) {
	    this.nf = Integer.parseInt(prio.split("nf")[1]);
	}

    }

    @Override
    public String toString() {
	return "news" + this.news + " ichi" + this.ichi + " spec" + this.spec + " gai" + this.gai + " nf" + this.nf;
    }

}
