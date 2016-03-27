package barqsoft.footballscores;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies
{

    //These Codes Dont Match Up With The Codes in myFetchService.
//    public static final int SERIE_A = 357;
//    public static final int PREMIER_LEGAUE = 354;
//    public static final int CHAMPIONS_LEAGUE = 362;
//    public static final int PRIMERA_DIVISION = 358;
//    public static final int BUNDESLIGA = 351;

    public static final int SERIE_A = 401;
    public static final int PREMIER_LEGAUE = 398;
    public static final int CHAMPIONS_LEAGUE = 405;
    public static final int PRIMERA_DIVISION = 399;
    public static final int BUNDESLIGA1 =  394;
    public static final int BUNDESLIGA2= 395;


    private static final String ARSENAL = "Arsenal London FC";
    private static final String MANCHESTER = "Manchester United FC";
    private static final String SWANSEA = "Swansea City";
    private static final String LEICESTER ="Leicester City";
    private static final String EVERTON = "Everton FC";
    private static final String WEST_UNITED = "West Ham United FC";
    private static final String TOTTENHAM = "Tottenham Hotspur FC";
    private static final String WEST_BROMWICH = "West Bromwich Albion";
    private static final String SUNDERLAND = "Sunderland AFC";
    private static final String STOKE_CITY = "Stoke City FC";


    private static final String SERIES_A = "Seria A";
    private static final String PREMIER_LEAGUE = "Premier League";
    private static final String CHAMP_LEAGUE = "UEFA Champions League";
    private static final String PREMIER_DIVISION ="Primera Division";
    private static final String BUNDES_1 ="1.Bundesliga";
    private static final String BUNDES_2 = "2.Bundesliga";
    private static final String UNKNOWN = "Not known League Please report";



    public static String getLeague(int league_num)
    {
        switch (league_num)
        {
            case SERIE_A : return SERIES_A;
            case PREMIER_LEGAUE : return PREMIER_LEAGUE;
            case CHAMPIONS_LEAGUE : return CHAMP_LEAGUE;
            case PRIMERA_DIVISION : return PREMIER_DIVISION;
            case BUNDESLIGA1: return BUNDES_1;
            case BUNDESLIGA2: return BUNDES_2;
            default: return UNKNOWN;
        }
    }
    public static String getMatchDay(int match_day,int league_num)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return "Group Stages, Matchday : 6";
            }
            else if(match_day == 7 || match_day == 8)
            {
                return "First Knockout round";
            }
            else if(match_day == 9 || match_day == 10)
            {
                return "QuarterFinal";
            }
            else if(match_day == 11 || match_day == 12)
            {
                return "SemiFinal";
            }
            else
            {
                return "Final";
            }
        }
        else
        {
            return "Matchday : " + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
        
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case ARSENAL: return R.drawable.arsenal;
            case MANCHESTER : return R.drawable.manchester_united;
            case SWANSEA : return R.drawable.swansea_city_afc;
            case LEICESTER: return R.drawable.leicester_city_fc_hd_logo;
            case EVERTON : return R.drawable.everton_fc_logo1;
            case WEST_UNITED : return R.drawable.west_ham;
            case TOTTENHAM : return R.drawable.tottenham_hotspur;
            case WEST_BROMWICH : return R.drawable.west_bromwich_albion_hd_logo;
            case SUNDERLAND : return R.drawable.sunderland;
            case STOKE_CITY : return R.drawable.stoke_city;
            default: return R.drawable.no_icon;
        }
    }


    public static int inversePositionForRTL(int position, int total){
        return total - position - 1;
    }

}
