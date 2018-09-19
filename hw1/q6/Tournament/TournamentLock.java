package q6.Tournament;

public class TournamentLock implements Lock {

	int id;
	public TournamentLock(int id)
	{
		this.id = id;
	}
	public void lock(int pid){
        PIncrement.turn[id] = pid;
        while(PIncrement.turn[id]==pid && PIncrement.testOpponents(pid,id))
        {}             
        
	}
	public void unlock(int id)
	{
		PIncrement.flag[id]=0;

	}
}
