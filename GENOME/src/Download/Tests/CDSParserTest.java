package Download.Tests;

import Download.CDSParser;
import Exception.OperatorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CDSParserTest {

    @Test
    void CDSParser() throws OperatorException {
        String s_CDS = "     CDS             complement(1552..2616)\n" +
                "     CDS             complement(2688..3668)\n" +
                "     CDS             4660..6212\n" +
                "     CDS             6282..6578\n" +
                "     CDS             6579..7266\n" +
                "     CDS             7340..7504\n" +
                "     CDS             7489..8181\n" +
                "     CDS             8187..8969\n" +
                "     CDS             9066..9416\n" +
                "     CDS             9424..10806\n" +
                "     CDS             10953..12854\n" +
                "     CDS             complement(12913..13401)\n" +
                "     CDS             13416..14553\n" +
                "     CDS             join(13416..14553, 13416..14553)\n" +
                "     CDS             join(7489..8181, 7489, 7489, 7489, 7489..8181)\n" +
                "     CDS             -1..2563589\n" +
                "     CDS             5..2563589\n" +
                "     CDS             join()\n" +
                "     CDS             join(complement(), 12)\n" +
                "     CDS             12..\n" +
                "     CDS             \n" +
                "ORIGIN      \n" +
                "        1 agatagaaac tgacctggct cacgccggtc tgaactcaga tcacgtagga ttttaatggt\n" +
                "       61 cgaacagacc aaccctcagg aactactgca cccctaggca atcccgatcc aacatcgagg\n" +
                "      121 tcgcaaactc ccttatcaat aagaactctc caagagaatt acgctgttat ccctacggta\n" +
                "      181 acttactcct ttaatcgccc tcttcccggc ggatccccac aaaggcaacg cccgcacaga\n" +
                "      241 agccgacact tcacgaatgc tccgaaaacg gagggtaact attctccgca gttgccccaa\n" +
                "      301 ctaaagcaac aagcacacag ccaggccacc tgatcggtaa actcacctcc tccaaaagaa\n" +
                "      361 gacgattagc ccccaaacag ccaaaccaac ttgtgcaacc cactaaagct cgatagggtc\n" +
                "      421 ttctcgtccc acgactacac ccccgcttct tcacgaggat atcaaattca agatttgagt\n" +
                "      481 aggagacagc tggacctccg tcttgccatt cataccagcc cccatttaaa gggcaaatga\n" +
                "      541 ttatgctacc ttcgcacggt caagataccg cggccgttta actaagtcac cgggcaggca\n" +
                "      601 ggacccttta tacataagaa aactttacaa agggcgatgt ttttggtaaa caggcgagat\n" +
                "      661 ccttgatttg ccgaattcct tctcctcaat tttataacct ttccgagcca cttgagttag\n" +
                "      721 cagacagaca aagacgagct acactttctc gcacattgaa gctcctccca tcaaaaatta\n" +
                "      781 ggactgctta acacacaaaa agccacttta ctctttacta atcccagcat tataactccc\n" +
                "      841 ataatagtat gagacttccc cataaataac cacggcataa aactcacctt gccaaaatat\n" +
                "      901 acatacacgc cactggctac aaaacgagct ttaacgccct catccggata gatgccatta\n" +
                "      961 atcctaccac tagtcagaac ccaattaacg tgtttcccag cgcgttttta acccatggtt\n" +
                "     1021 aaggctttta cctcctcaac aaggaagctt atccctcctt gtctacactc cgaatttcta\n" +
                "     1081 tcaaggatta actattacct cttacggata attacatccc caatagccaa acaaatatag\n" +
                "     1141 ctgacaccaa tttccgggga aaccagctat cattgagcac gataaacatt tcacacctag\n" +
                "     1201 tgtaccctcg aaccttacta ttgcaacagt aaaagctttc ttctcacaag gaagagtaca\n" +
                "     1261 ctaactcgct caaattcggg tacggcaact cctcaccaga atactcaccg aaccatcata\n" +
                "     1321 caaaaggtac aggacttcat cctttcttct ttacccttaa atgtttcacg atatttcaac\n" +
                "     1381 tttccctcac ggtactaccc ctttagctct cactacaagt ttcactgaca acctactact\n" +
                "     1441 cccacttgtt ccaaactatc aatattttga taaacctaaa acatcacggt tttattcttt\n" +
                "     1501 cttcacctct ataaggctag cctttaaaaa tctaattact ccacacaaaa tctacaacaa\n" +
                "     1561 ggaaacaaac acagggaaac tcaccaagcc tagtaaactc accctcaaac cccaacaaag\n" +
                "     1621 aattagaccc cgagccgtag gaacacccaa tcccccatcc tgcagccctc gccagccaaa\n" +
                "     1681 gagaactaga gaatgctgag gaaacaacaa caagctcctt ttaaaggaca cacgaagata\n" +
                "     1741 aaagaacaag ctaagcagac tccctgcaac taaaaaacct cttaccagaa cactacccct\n" +
                "     1801 ttcaagaaga cacttcagag agacgaactt tattaggaag ccgaaaagag gaggcaaaac\n" +
                "     1861 ccccagggaa agcacaccca aaacaatacc ggaactcttc ccaacattaa agtatgaaac\n" +
                "     1921 acgacctaca tgagctaaag tcttcaaatc aaattctctc gcaatcaaaa acaccctgga\n" +
                "     1981 attagtaatt atgtaaatca gcaacataac acaaccggca ttagcagaat accccacagt\n" +
                "     2041 tgagcaaatc caccccatat gtgccaccga tgaaaaagcc aaaatcttcc gcacctgcgt\n" +
                "     2101 ctgattcaaa cctccccacc cacctattag cacagaaaac acagccagac atgacaatac\n" +
                "     2161 tcgaatatct accctatcaa tcacatacac caaaacagaa aaaggggcca gcttctgcca\n" +
                "     2221 agttgacaga accaaaccct ctaagaagcc aaccccctgg acaacatctg gaaaccagta\n" +
                "     2281 atgacacgga aagagcccca acttgagacc tatggccaga gtgatcaata aggaagaaac\n" +
                "     2341 cttaagcagg ggttggccaa ccaaccacga agaactaaac caagcttgta taacaacaac\n" +
                "     2401 attaagtata acagcagccc tcacggactg caccaaaaaa tacttcacgg acgactctac\n" +
                "     2461 cttccggggt aaaaaccccc cacacagaat gggcaaaata gataaagtat tcacctccaa\n" +
                "     2521 tcccacccac aaagtaaacc agtgatggct tcttaaaaca acaaaagtcc caaagacaac\n" +
                "     2581 tctaacaacc aacacaataa aaaccctacg atgcataaaa cctggaagga atttaacctt\n" +
                "     2641 cagcaaccta attatcagct aggaacccct acttcagggt acaggttcta tactaagacc\n" +
                "     2701 aaactaggtg gcaaaatatc aacagaaact aacaacaccc tataaaatac taacactcct\n" +
                "     2761 aaactcaagg gtaaatactt cttccaagtc aagtacatca aatggtcata tcgaaaccga\n" +
                "     2821 ggataagatg cccgaactca tagaaacaag acaaccaaaa atatagtctt taccctcacc\n" +
                "     2881 attagaaccc ctacagggaa aacaccccta aaaggactcg aacccccaaa aaagaggacc\n" +
                "     2941 actgaaagta aattcataaa aattattttc ccgtattctg ctataaaaaa catagcaaaa\n" +
                "     3001 ggcccaccag catattctac tttataccct gaaacaattt ccgactcccc ctccgtcaaa\n" +
                "     3061 tcaaagggag cacggttagt ctcggctaat gtagaaacaa accataccac aaacaaaggt\n" +
                "     3121 aagcaacaaa agaccaacca agaacctccc tgactttttt cgatgaccct aaggctgaag\n" +
                "     3181 ccccccgaga acactactac ccctaatagg atcaacccta aacttatctc ataagagacc\n" +
                "     3241 gtttgagcaa cagcccgaac agcccccaaa aaggaataat tcgaattaga tgcccagccc\n" +
                "     3301 gatcccagca aagcataaac agacaacctg gataacccca tcactaaaac aagcgacaat\n" +
                "     3361 ttaacctcca acacacaaaa gccgactgga ataagtgacc acaagaaaag cgcaatccct\n" +
                "     3421 agaaaaagaa taggagacaa aaaaaacaaa taaggcgaag ctttagaagg ctttaaagtc\n" +
                "     3481 tcctttatca acagcttgaa cccgtctgct atcggttgta ataaaccaaa cggccccacc\n" +
                "     3541 acattaggcc ccttacgaaa ttgcatataa ccaaggacct ttcgctccac taaagtcaac\n" +
                "     3601 aaagccactg ccagcagcac cggaattata aaaacaactg atttagtaat aaaaactagc\n" +
                "     3661 ccctccacag ctaaagaaag gactcgaacc ttccatagag aaaccttaga cttcttgcat\n" +
                "     3721 taccactttg ctacttcagc tatttctata tataaggatt atctgacata cttcacctct\n" +
                "     3781 gattacaaac agagacccct tagttggaag ccaagaatac tgatgtactc gtgaagtcta\n" +
                "     3841 gcgagaggag gaatcaaacc ccccttcttg gatttacaat ccaccacact aacctctgcc\n" +
                "     3901 atctcgccaa agagctagtt aaaccgataa caccagaatg tcagactgga attgctggct\n" +
                "     3961 gataacccag cgctctttga aataaaggga ggtatctatc cttccatttt tggggtatga\n" +
                "     4021 acccaaaagc ttctattagc ttactttact tacatccttt ctaggttaga gcttgaccaa\n" +
                "     4081 gcatctcttt tacacggaga agatatctgc gcaaaccaga ttaccttgaa agttctggcg\n" +
                "     4141 gatgttaccc gcatctatag atttgcaatc taaaatgtta attacactac aaaacccagg\n" +
                "     4201 ggttaaaaaa ctttcatttt tatttaaagc tttgaaggct atcagtttta attaacttaa\n" +
                "     4261 acccctcatt agtgaattta gtttaataaa aacatttgac ttgcacttaa aaactctaag\n" +
                "     4321 tttaaacctt agaattcaca aactaagaga aggaattgaa ccttcgatat tagatcctaa\n" +
                "     4381 atctaatgca gtaaccactt tgctatccta accctgagtc gacaagtatc gatcttgtta\n" +
                "     4441 tctcctggtt aacggccaat tgccttccca ttaggctacg acccaaatag aaagtagttt\n" +
                "     4501 aacaggtaaa acaaagaatt ttgatttctt caacataagt tcaaccctta tctttctagt\n" +
                "     4561 cagaaaaata ggactccaca cctataccag caactcccaa agctaccatt ctcactaaac\n" +
                "     4621 tattttctgt ttaccctata tataataaaa ttataaacca tgcaactgag gcgatgattt\n" +
                "     4681 ttttcaacaa aacacaagga cattggaaca ctatatctaa tattcggagc ctgggcagga\n" +
                "     4741 atggttggaa ctgccatgag agtaataata cgaacagaac tggcccaacc aggatcactc\n" +
                "     4801 ctccaagacg accaaatata caacgtaata gttacggccc acgccctagt aatgatattc\n" +
                "     4861 ttcatggtaa tgccaatcat gattggcgga tttggcaact gactaatccc cttaatgatc\n" +
                "     4921 ggagcacccg atatggcctt tccccgaatg aacaacatga gcttctggct agtccccccc\n" +
                "     4981 tccttcctgc tactcttagc atcagccgga gttgaaagag gtgctggaac cggatgaacc\n" +
                "     5041 atctaccctc cactgtctag tggcctagcc cacgctgggg ggtcagtaga ccttgcaata\n" +
                "     5101 ttttccctcc accttgcagg agcatcctct atcctagcct ccataaaatt catcactact\n" +
                "     5161 gtcattaaca tgcgaacccc tggtatctca ttcgaccgcc taccattatt tgtctgatct\n" +
                "     5221 gtattcgtga cagcattctt actcctcctt tcccttcccg ttctagctgg agccataacg\n" +
                "     5281 atgctcctca ctgatcgaaa agtgaacaca accttcttcg acccagcagg aggaggggac\n" +
                "     5341 cctattctat tccaacacct tttctggttc ttcggacacc ctgaagtcta cattctcata\n" +
                "     5401 cttccaggat ttggaatgat atcccatgta attgctcact actcaggtaa gagagaacct\n" +
                "     5461 tttggttacc taggtatggt ctacgctata gtatctattg gaatactagg gttcctggtg\n" +
                "     5521 tgagcccacc acatgtttac agttggtatg gacgtagaca ctcgagcata ctttacggct\n" +
                "     5581 gcaacaatga taatagctgt gcccacagga attaaggtct tcagatggat ggcaacacta\n" +
                "     5641 caaggcagaa accttcgatg agacacacca ctactttggg ccctcggatt tgtattctta\n" +
                "     5701 tttaccatag gaggactaac gggtgtaatc ctagcaaaat catccatcga cgtaatcctc\n" +
                "     5761 cacgatactt actacgtagt cgcccacttc cactacgtac tgtcgatggg agcagtattt\n" +
                "     5821 gctatctttg caggatttac acactgattc cccctcttct caggagtagg acttcacccc\n" +
                "     5881 ttatgaggaa agatccactt cgcattaatg tttataggag taaacctcac tttctttccc\n" +
                "     5941 cagcacttcc tgggactagc cggtatgcca cggcgatact ctgattaccc agacgcatac\n" +
                "     6001 accctttgaa acactatctc atccataggc agcaccatat ccctcgtagc aacgctgata\n" +
                "     6061 ttcttattca ttatttggga agccttcacc tcccaacgaa caacaattca acccgaattt\n" +
                "     6121 gcaccttcct cactagaatg gcaatactcc tcctttcccc cctcccatca taccttcgac\n" +
                "     6181 gaaattccag caactgtata cttaattaag tatgaggatt agtttaataa aaacctttga\n" +
                "     6241 cttcgactca aaaaacttca gttacaccct gaaatccttt tattacaccc atctttatta\n" +
                "     6301 gtactatatc catattttac ctaggattac taccaatatt ttttaaccgg ctacacttct\n" +
                "     6361 tatctatcct gctctgcctg gaactcctcc taatatcact attcctaaga ataacaatat\n" +
                "     6421 gagcactaaa agccggaacc aacttcttcg tggcaaaaaa cctcatcctt cttacccttt\n" +
                "     6481 ctgcctgcga agcaagagcc ggactctccc taatggtagc actctcacga actcacaaat\n" +
                "     6541 tagacttagt tatagcacta aacatattac aacaataaat ggcaaattgg acccaactag\n" +
                "     6601 gactacaaga cgcatcttcc cccctgatgg aagaactaat ctactttcat gactacaccc\n" +
                "     6661 taatcatcct cactctaatc acaatactag tcttctacgg attggcatcc ctgctattct\n" +
                "     6721 cctccagaac aaaccgattc tttttagaag gacaaggatt agaaaccata tgaacaatca\n" +
                "     6781 tccctgccat catcctcata tttatagcac tcccatccct ccaacttctc taccttatgg\n" +
                "     6841 acgaggtaaa aaaccccttc ctcactataa aggcgatagg acaccaatgg tactgaagat\n" +
                "     6901 acgaatacgc agattaccgt gaattagaat ttgactcata catgattcct acttcagacc\n" +
                "     6961 tttcctccgg catgccccgc ctactaaaag tagacaaacg attgatactg cccgcccaaa\n" +
                "     7021 cgccaatccg cgtattagta tcctccgcag acgttctcca ctcctgggca aacctttccc\n" +
                "     7081 taggagtaaa gatggacgcg gtaccaggac gactaaacca ggtaaaattt ttcatttccc\n" +
                "     7141 gatgcggcat attctatggc cagtgctcag agatttgcgg agcaaaccat agattcatgc\n" +
                "     7201 ccatagttat tgagaccaca aaattttcct catttgaaac atgggtatct aacttcctaa\n" +
                "     7261 cagaatcttt ggtaagctaa tacggaagca tcaaactctt gacttgaata aaagtggaga\n" +
                "     7321 cccgcccact accaaagaaa tgccacagct aaacctagct tgatgactat ttaaattctt\n" +
                "     7381 aattagatga gcaacattac ttgtagtctt tactgcctta ctcagcacac ccccaataga\n" +
                "     7441 aaagcctacc aaaatcacaa cccaaactaa gttacacccc ttaaaaaaat gaacctgaaa\n" +
                "     7501 ctaaaaaaca tattcggcca attctccccg gacatcgtag ccctaattcc catgaccctc\n" +
                "     7561 atcgccttgg ccattaacct aggatgactc tctctaacaa gaagaacaaa ctgactcctc\n" +
                "     7621 acccgaagtg gaactttgat ccaaaccttc aagcagggaa ctctaagaat actattccag\n" +
                "     7681 caagccaacc ccaaaattgc accctgaatc ccagccttta cctctgtatt tatattcatc\n" +
                "     7741 ctatcagtaa aagtattagg actactaccg tatgccttca cttctaccag ccacatatca\n" +
                "     7801 cttacttact ccataggcat tcccttctga atgtccgtca acgtccttgg attctatcta\n" +
                "     7861 gcattcaaaa gtcgactaag acacctagta ccccaaggta ctccccccta cctaatcccc\n" +
                "     7921 ctgatggtaa taatagaaac aatcagacta ttcgcacaac ccatcgcctt gggcctacga\n" +
                "     7981 ttagccgcca acctaacagc agggcatctt ttaatatacc tcctctccac agcgatttga\n" +
                "     8041 gtcctagcaa gatcccccct tatagctgga ataaccctct caatattttt cctcctattc\n" +
                "     8101 ttactagaaa taggagtagc ctgcattcag gcctacgtgt ttaccgcact agttaaattc\n" +
                "     8161 taccttgccc aaaacctata aaaatcatgg cccaatcaca cccataccac ctagtagacc\n" +
                "     8221 aaagaccctg acccctcact ggggctatta gtgccctcat gatgacctcc ggtctgatac\n" +
                "     8281 tgtgattcca cgtaaaaaga aacttactgc taattgcagg aactctactt cttaccctaa\n" +
                "     8341 ctatcctaaa atgatggcga gacgtaatac gggaagccac gtttcaaggc agacacaccc\n" +
                "     8401 taccagttaa cactggacta cgatacggta tgatactttt tattacctca gaagtttgct\n" +
                "     8461 tcttcttcgc tttcttctga gcattcttcc acagcagcct agcaccaacc atcgaactag\n" +
                "     8521 gcgtatcatg accccccact ggaataaacc caatcaaccc ctttctagtc cctcttctta\n" +
                "     8581 acactgccgt cctactctca tccggggtaa cagtcacttg agcccaccac agaatcctag\n" +
                "     8641 ccgggaaccg cacagaatcc atacaagccc tattcctaac tatagcccta ggtatatact\n" +
                "     8701 ttaccaccct acaagcctga gagtattacg actccccatt taccatcgcc gacagagtgt\n" +
                "     8761 atggctctac attttttgta gccaccggct tccatgggct ccacgtactc ataggaacaa\n" +
                "     8821 ctttcctctt agtatgcttc atacgactaa tattcttcca cttttccaat aaccaccact\n" +
                "     8881 ttggcttcga agcggccgcc tgatactgac acttcgtaga cgttgtatga ctattccttt\n" +
                "     8941 acgtctgcat ctattgatga gggtcctaag agaagaaagg actcgaacct ttatctttct\n" +
                "     9001 agtttcaagc taaacgcatc ccgctctgcc acttctcccc aaaaatcctt atatacccaa\n" +
                "     9061 taaaaattta caaatttgcc atcaccctga catcaatcct cctgataaca ggattaatca\n" +
                "     9121 ccaccgcagc ccacgcccta cctagccgaa aaatagacct agaaaagtcc tcaccctacg\n" +
                "     9181 aatgtgggtt tgaccctttg aaacacgcac gcataccatt ctctttccga tttttcctgg\n" +
                "     9241 ttgccatctt atttctccta ttcgacctgg aaattgccct attattcccg ttccccgtaa\n" +
                "     9301 gaacattctt tatgtcccct aaaacctcac taggactggc aatagcattc ctactaatac\n" +
                "     9361 ttacacttgg gctagccttc gaatgatctc aagggggtct cgactgagcc gaataagaaa\n" +
                "     9421 ctaatgatat atcttatctt agcctcctcc tcaagcctca tcctatcctg ggctgtgccc\n" +
                "     9481 cgaacactac tatgaccctc aaccataatc tcttcgctta tcctctccag gctaactctt\n" +
                "     9541 aacctagccc aaaaacacac atcttcgtta tgacacaata tctccgctaa cttcgccacc\n" +
                "     9601 gaccctctgg ccacacccct aataatccta agcatatgac ttggcccttt gtcccttatt\n" +
                "     9661 gccagaataa aaacaataaa gagcctacca ctaataaaac agcgcacctt catttctcta\n" +
                "     9721 atatttgtta tcctcactgc cctaatcctc acgcttgcct cactcgaact aaccctattc\n" +
                "     9781 tacatcgcat ttgaaacaac ccttcttcca acgctaattc tcatatcccg ctggggggcg\n" +
                "     9841 aaaaaggaac ggtaccaggc cagtacatac ttccttttct atactctcgt gggttcccta\n" +
                "     9901 cccctactca tagccatcat aagcatttac accacaacta aatcatccct actacctact\n" +
                "     9961 acttccctta aaaaaaaaat aacatcacta cccaccacac taacctctct atgatgagtc\n" +
                "    10021 ttctgcctca tcgcctttgt aataaagatg ccaatttatg gattccacct ttggcttccc\n" +
                "    10081 aaggcccatg tagaggctcc tatagctggg tcaatgatcc ttgcagccgt acttctaaag\n" +
                "    10141 ctagggggat acggactcac acgaactatt acaatatttg ttatacccgc ctctataaaa\n" +
                "    10201 atatcctttc ccctcacgac cttctgcata tgaggcgccc taataacaag aataatctgc\n" +
                "    10261 ctccgccaaa cagacttaaa ggccttaata gcctactcat ccgtgggcca catgaggtta\n" +
                "    10321 gtagcttccg gcatattttc aatatcccta tggggaataa aaggtgccct cactctcatg\n" +
                "    10381 attgcccatg gattaatatc ctcagccctg ttttgccttg caaaactcct gtacgaacga\n" +
                "    10441 aacaccacac gtaccctatc aataacccga ggtttcaagc tagtgacccc actactcact\n" +
                "    10501 gtatgatgaa taataacgtg cgtaacaaac ctaggacttc cccctactcc taacctaatc\n" +
                "    10561 ggggaactcc ttatcatatc cactattata gactgaaaaa tcctatccac acccataatt\n" +
                "    10621 accacggcaa caatatttgg agccctctac tccctcctta tattaactaa caccaataaa\n" +
                "    10681 aactccttcc caagctttat tacaaaaact cccccggtaa ccacttcaga acacacccta\n" +
                "    10741 ataacccttc atcttatccc cctaacattc atcatactta aaccctccta catactaata\n" +
                "    10801 aaatagcacc cacgactaaa gtagtttaat aaagacacta atttgtggca ttagaattgc\n" +
                "    10861 cagttaaaac ctggcctata gtccgaaact tattggctta ttctagtcct gctaagtcca\n" +
                "    10921 agaatctgta gttcaattct atagaagttt cgatggtaat agaaatcaca agaataacca\n" +
                "    10981 caaccataaa aatcacgatc ataaccatac tgcttatctc catattttgc tttcaaagcg\n" +
                "    11041 atctattgcc ccataagggg ctacctaaca tcaacttcgg aacacttcat gacaaaaagt\n" +
                "    11101 tcaggtacac acgcaaaaga agactattct tctcatctat actaaagaga tcagccctcc\n" +
                "    11161 tttcactcat acccctcttt ttaaaaataa aatttgacac cccagaaaca atagtttcca\n" +
                "    11221 tagccgaatg attacccaaa aacgcacacc tcataagtat tgaactacga tttgaccttt\n" +
                "    11281 cattcaaact attcttttca gttgcattat tcgtctcttg atcaatccta gaattctcac\n" +
                "    11341 actactacat ggcacaagac ccagcaccag gcaaattctt ccgtctctta ataattttcc\n" +
                "    11401 tactaaaaat gatcatactc acctccacta aaaacgtatt ccttctgttt ataggctggg\n" +
                "    11461 aaggggtcgg attcttatca ttcttattga taaggtggtg aaccacacga gcaagagcaa\n" +
                "    11521 aaaaatctgc cctccaagca gtaatataca accgaatagg agacataggc atccttttat\n" +
                "    11581 ttttctccct gagggttaca acctttaatt cctggaccct atcagagata tttgtcctac\n" +
                "    11641 agcccacaaa ctctctaggg aatattttac tagtttgggc cctcatagct gctgcaggga\n" +
                "    11701 agtccgccca attcggacta cacccctgac tcccagcagc catggaggga cccacccctc\n" +
                "    11761 tatcagccct cctccacagc tctacaatgg tagttgcagg aatattccta ctaatccgcc\n" +
                "    11821 gtggcccggc ctattcagac ataagagcct ttaaaacatg atgcctgata cttggcttta\n" +
                "    11881 tcacagcaat attggctgca accacggcca tatcccagca cgacataaag aagattgtag\n" +
                "    11941 catactccac caccagacaa ctaggactca tgatggtcgc cataggccta aaccaaccag\n" +
                "    12001 agatcgcact attccacgtt tgcacacacg cattctttaa ggcaatgcta ttcctatctt\n" +
                "    12061 ctggcagaat tatccacaga ctaaaggacg aacaagacat acgaaagatg ggaggactct\n" +
                "    12121 acctgatact ccctaacaca actgcctgca ttattctagg gagacttgcc ctctctggta\n" +
                "    12181 caccattcct tgctggattc tactccaagg acctaatcct agaactgggc ctctctagac\n" +
                "    12241 tatcaaacct aacgggaatt atcttagctc tcttggcaac ccttttgacc gcagcttact\n" +
                "    12301 cattccgcat tatacatttc tgcttcattg gaaacccgac cttctcaccg ctctccccca\n" +
                "    12361 ccagagaaga aaaaactaaa ctaaccaaag cattaaaacg actagccgct ggtacgataa\n" +
                "    12421 tatcaggatg ggtaatatca aacttcatcc tgcaaccacc caaaatcacc gtcagactcg\n" +
                "    12481 cccttaagag tctggccacc ctattaaccg tctccgggat aatattaaca atatcactgc\n" +
                "    12541 tccacactct ttccctaaaa atgccccccc aaaacctcat aaacaccaaa acgttcacca\n" +
                "    12601 caaaacaatg attctacgaa cacacgtccc acactatact cacaaccttg tctttctcgc\n" +
                "    12661 tctccctaac aggaaggacc caaagaatag accgagggtg aagagaaaac ctgggcgcac\n" +
                "    12721 aaggaataaa tctagcttct tcggaaatct cccaaaaata tcaactggcc caaacaggct\n" +
                "    12781 acattaagca ataccttctc ctttccatgt ccacctttac tgtcatagcc ataatctccg\n" +
                "    12841 ccgctctcct ataatagcca agccagcgga aactataatt atactcgata acaactaaat\n" +
                "    12901 atatagctag ctctacaggg ccttcaatat tttatattcc gccccataag taatcactag\n" +
                "    12961 tgcaactact aaagcaaaca agaatacaaa cccccctact aacaaataac cccccaatac\n" +
                "    13021 tttatacaaa gtaccacccc caacaagatc cctttctact accacagacc accccccgag\n" +
                "    13081 cgaaaagccc aagaaaggat caaacctgac aaaaacccaa acaacgccca cgccccttaa\n" +
                "    13141 gagaaacacc tcattcaaat tactcactac tgggtaccgc tctgcagaaa tagccgtaga\n" +
                "    13201 ataaacaaaa acaaccagca ttccccccat ataaataaga acaagcacca gagccacaaa\n" +
                "    13261 agaggaccca agaaacccgc aaaggacaca ccccgacacg gccactaaaa ccaggcctag\n" +
                "    13321 ggcaccaaaa taaggggaca acctataaaa caccagagta cttcccaaaa gcataacaac\n" +
                "    13381 caatacaata taaaaaacca ttatatatag aaatcatgac aggccctctc cgaaagagac\n" +
                "    13441 accccctatt taaagtagtg aaaaattttc taatagacct cccctcccct agaaaactat\n" +
                "    13501 ccatatggtg aaaatttgga tccctattag gcctttgcct aattatccaa ataatcacag\n" +
                "    13561 gcctatttct agctatgcat tatacctcag acatttcctt agccttctca tccatcagcc\n" +
                "    13621 acatatgccg agacgtaaac tacggttgac ttctacgaaa tattcacgcc aaaaccgcct\n" +
                "    13681 ctttcttttt cctttgcatt tattttcaca taggccgagg attatactac ggctcctaca\n" +
                "    13741 taaaaaagga agcctgaaaa ataggcgtca tactccttct cctcactatg ctaacagctt\n" +
                "    13801 tcgtcggata cgtcctcccg tggggtcaaa tgtcgttttg aggagcaaca gtaatcacaa\n" +
                "    13861 acctcgtgtc tgcccttccc tacataggga catcaatagt ccaatgactt tggggtggat\n" +
                "    13921 tctcagttga caacgccact cttacccgat tctttgcctt ccacttcctg ttccctttta\n" +
                "    13981 ttatcaccgc tctctcaatc atccatctat tcttcctcca ccaaaccggt tccaacaaag\n" +
                "    14041 ccacagggat aggatcaaag tttgataaga ctccgttcca cacatacttt tcttacaagg\n" +
                "    14101 acctaacagg gtttatagcc ttatttaccc taataagcgc aatagttctt ctatccccaa\n" +
                "    14161 ctttactcag agaccctgaa aaattcaaac ctgccaaccc cctagttaca ccagtccaca\n" +
                "    14221 tccaaccgga atgatacttc cttttcgctt acgcaatcct tcgatctatc cctaaaaagt\n" +
                "    14281 tagggggcgt aatagcccta ttagcatcaa tccttgtttt attcatggtc ccaatcctcc\n" +
                "    14341 atacatcaaa taaccaagcc agtacctttc gacctgtttc acagagactc ttctgatgtc\n" +
                "    14401 tcaccaccat cttcctgttc ctaacgtgaa tagggagaca acccgtagaa gacccattca\n" +
                "    14461 tcctccttgg acaaatagcc tcaacagctt acttctcctt attcctgctt gcaattcccc\n" +
                "    14521 taacgtcaga actagagaaa aagctgatct tctgcaaaga tagcttaata gaaaagcaaa\n" +
                "    14581 gcactgaaaa cgcttaaata aaggttcgag tccttttctt agcaaccgat ttggtcctag\n" +
                "    14641 tccttccctc aactttctcc aggatgatac atgcaaagcc ctcaattcta cggtgagctc\n" +
                "    14701 aatctgcttc tggtctcccc cgagatacca gaacaccaaa aatagcatca ggttaccgcc\n" +
                "    14761 tatcgtaacc caagacgcta aggccagcca aacaaaaata gcccacacct gcagtacttg\n" +
                "    14821 acattacaca ataagcgcta gcttgatgta gccatagagg aaaaaaccgg taaattatgt\n" +
                "    14881 gccagccacc gcggttatac ataagatttt agttgagggc ctcggtaaaa aggtgataaa\n" +
                "    14941 acagcaccag agtcctcgcc ccctcaagca gtagtaagct aacaggaccc taagaactat\n" +
                "    15001 cctaccaaac cctgccctat ccaaaccctt aagattcatc accaaagctc aaagataaac\n" +
                "    15061 tgggattaga taccccatta tatgagccct aaaacaactt aagcacctga gaactacgaa\n" +
                "    15121 caaaagttta aaactcaaag gacttggcgg ttttctagac ctccctggag gagcttgcta\n" +
                "    15181 tctaaccgat aacccacgaa acacctcacc agcctttgca aaccgcagcc tgtataccat\n" +
                "    15241 cgtcgtcagt ctacccttta aagacagtat acctaatgga gtgatctcca tatcctcctc\n" +
                "    15301 cacgtcggat cgaggtgcag ctaataggcc ggggataagt gagctacaat aactaatcaa\n" +
                "    15361 cacacccttt aagccaacat atggacaagc ccatgaaaac cgggactcaa aacaggattc\n" +
                "    15421 agcagtaacg cctacaaggg aataggtgtg aagcacacag ctctagaatg cgcacacatc\n" +
                "    15481 gcccgtcact ctcgtctaca gaggagaaaa gtcgtaacaa agtaggcgta ttggaaaatg\n" +
                "    15541 cgcctggtac aaattcttat agttgagcca caacagcagc ttttcacgct acaagcttgg\n" +
                "    15601 gttagacccc caataagaat tacgcgacca gcctcaaaag ctaacgggta agcaattaat\n" +
                "    15661 cttgtaaatt aaaaggcgaa ggttcgagtc ctttttgagg ctccccataa cccccccccc\n" +
                "    15721 ccccctccgg gggggttata ctcgcataag cttgtgttct aggaaagcta tatagggcca\n" +
                "    15781 aggcgttatg agaccttcct tatggggcaa tagatcgctt tgaaagcata ggcaaaatcc\n" +
                "    15841 cttatttctt atatttccgc tccgcggggg atagttgggg taaggctaca ttagctggaa\n" +
                "    15901 ccacgtccag gtgtttccct tcttactttg acaagctcct ttctgctaca ggattataaa\n" +
                "    15961 ccaaatgaca attggacaca atccaccata ggtccctagg cccctacagc actgagtccc\n" +
                "    16021 cttacaacca cgaaagacca aaacaaaact cagggccatg caaccgtgcc ccataaacga\n" +
                "    16081 gacttaaacc agactctttt attcttctta ctacctgaaa tcgacctaca accacaggca\n" +
                "    16141 tcaaaaccac aatctttatt ttacaacaga accttaaaag gtgtttacct taaagcattg\n" +
                "    16201 acaccacccc tccaatcctt tcgtactaag aggagccctt atttccataa tcgt\n" +
                "//";
        CDSParser parser = new CDSParser(new StringBuilder(s_CDS), "NC_TEST");
        parser.parse();
        assertEquals(21, parser.getTotal());
        assertEquals(11, parser.getValid());

        assertTrue(0 == parser.getSequences().get(0).toString().compareTo("ATGCATCGTAGGGTTTTTATTGTGTTGGTTGTTAGAGTTGTCTTTGGGACTTTTGTTGTTTTAAGAAGCCATCACTGGTTTACTTTGTGGGTGGGATTGGAGGTGAATACTTTATCTATTTTGCCCATTCTGTGTGGGGGGTTTTTACCCCGGAAGGTAGAGTCGTCCGTGAAGTATTTTTTGGTGCAGTCCGTGAGGGCTGCTGTTATACTTAATGTTGTTGTTATACAAGCTTGGTTTAGTTCTTCGTGGTTGGTTGGCCAACCCCTGCTTAAGGTTTCTTCCTTATTGATCACTCTGGCCATAGGTCTCAAGTTGGGGCTCTTTCCGTGTCATTACTGGTTTCCAGATGTTGTCCAGGGGGTTGGCTTCTTAGAGGGTTTGGTTCTGTCAACTTGGCAGAAGCTGGCCCCTTTTTCTGTTTTGGTGTATGTGATTGATAGGGTAGATATTCGAGTATTGTCATGTCTGGCTGTGTTTTCTGTGCTAATAGGTGGGTGGGGAGGTTTGAATCAGACGCAGGTGCGGAAGATTTTGGCTTTTTCATCGGTGGCACATATGGGGTGGATTTGCTCAACTGTGGGGTATTCTGCTAATGCCGGTTGTGTTATGTTGCTGATTTACATAATTACTAATTCCAGGGTGTTTTTGATTGCGAGAGAATTTGATTTGAAGACTTTAGCTCATGTAGGTCGTGTTTCATACTTTAATGTTGGGAAGAGTTCCGGTATTGTTTTGGGTGTGCTTTCCCTGGGGGTTTTGCCTCCTCTTTTCGGCTTCCTAATAAAGTTCGTCTCTCTGAAGTGTCTTCTTGAAAGGGGTAGTGTTCTGGTAAGAGGTTTTTTAGTTGCAGGGAGTCTGCTTAGCTTGTTCTTTTATCTTCGTGTGTCCTTTAAAAGGAGCTTGTTGTTGTTTCCTCAGCATTCTCTAGTTCTCTTTGGCTGGCGAGGGCTGCAGGATGGGGGATTGGGTGTTCCTACGGCTCGGGGTCTAATTCTTTGTTGGGGTTTGAGGGTGAGTTTACTAGGCTTGGTGAGTTTCCCTGTGTTTGTTTCCTTGTTGTAG"));
        assertTrue(0 == parser.getSequences().get(1).toString().compareTo("GTGGAGGGGCTAGTTTTTATTACTAAATCAGTTGTTTTTATAATTCCGGTGCTGCTGGCAGTGGCTTTGTTGACTTTAGTGGAGCGAAAGGTCCTTGGTTATATGCAATTTCGTAAGGGGCCTAATGTGGTGGGGCCGTTTGGTTTATTACAACCGATAGCAGACGGGTTCAAGCTGTTGATAAAGGAGACTTTAAAGCCTTCTAAAGCTTCGCCTTATTTGTTTTTTTTGTCTCCTATTCTTTTTCTAGGGATTGCGCTTTTCTTGTGGTCACTTATTCCAGTCGGCTTTTGTGTGTTGGAGGTTAAATTGTCGCTTGTTTTAGTGATGGGGTTATCCAGGTTGTCTGTTTATGCTTTGCTGGGATCGGGCTGGGCATCTAATTCGAATTATTCCTTTTTGGGGGCTGTTCGGGCTGTTGCTCAAACGGTCTCTTATGAGATAAGTTTAGGGTTGATCCTATTAGGGGTAGTAGTGTTCTCGGGGGGCTTCAGCCTTAGGGTCATCGAAAAAAGTCAGGGAGGTTCTTGGTTGGTCTTTTGTTGCTTACCTTTGTTTGTGGTATGGTTTGTTTCTACATTAGCCGAGACTAACCGTGCTCCCTTTGATTTGACGGAGGGGGAGTCGGAAATTGTTTCAGGGTATAAAGTAGAATATGCTGGTGGGCCTTTTGCTATGTTTTTTATAGCAGAATACGGGAAAATAATTTTTATGAATTTACTTTCAGTGGTCCTCTTTTTTGGGGGTTCGAGTCCTTTTAGGGGTGTTTTCCCTGTAGGGGTTCTAATGGTGAGGGTAAAGACTATATTTTTGGTTGTCTTGTTTCTATGAGTTCGGGCATCTTATCCTCGGTTTCGATATGACCATTTGATGTACTTGACTTGGAAGAAGTATTTACCCTTGAGTTTAGGAGTGTTAGTATTTTATAGGGTGTTGTTAGTTTCTGTTGATATTTTGCCACCTAGTTTGGTCTTAGTATAG"));
        assertTrue(0 == parser.getSequences().get(2).toString().compareTo("ATTACACCCATCTTTATTAGTACTATATCCATATTTTACCTAGGATTACTACCAATATTTTTTAACCGGCTACACTTCTTATCTATCCTGCTCTGCCTGGAACTCCTCCTAATATCACTATTCCTAAGAATAACAATATGAGCACTAAAAGCCGGAACCAACTTCTTCGTGGCAAAAAACCTCATCCTTCTTACCCTTTCTGCCTGCGAAGCAAGAGCCGGACTCTCCCTAATGGTAGCACTCTCACGAACTCACAAATTAGACTTAGTTATAGCACTAAACATATTACAACAATAA"));
        assertTrue(0 == parser.getSequences().get(3).toString().compareTo("ATGCCACAGCTAAACCTAGCTTGATGACTATTTAAATTCTTAATTAGATGAGCAACATTACTTGTAGTCTTTACTGCCTTACTCAGCACACCCCCAATAGAAAAGCCTACCAAAATCACAACCCAAACTAAGTTACACCCCTTAAAAAAATGAACCTGAAACTAA"));
        assertTrue(0 == parser.getSequences().get(4).toString().compareTo("ATGAACCTGAAACTAAAAAACATATTCGGCCAATTCTCCCCGGACATCGTAGCCCTAATTCCCATGACCCTCATCGCCTTGGCCATTAACCTAGGATGACTCTCTCTAACAAGAAGAACAAACTGACTCCTCACCCGAAGTGGAACTTTGATCCAAACCTTCAAGCAGGGAACTCTAAGAATACTATTCCAGCAAGCCAACCCCAAAATTGCACCCTGAATCCCAGCCTTTACCTCTGTATTTATATTCATCCTATCAGTAAAAGTATTAGGACTACTACCGTATGCCTTCACTTCTACCAGCCACATATCACTTACTTACTCCATAGGCATTCCCTTCTGAATGTCCGTCAACGTCCTTGGATTCTATCTAGCATTCAAAAGTCGACTAAGACACCTAGTACCCCAAGGTACTCCCCCCTACCTAATCCCCCTGATGGTAATAATAGAAACAATCAGACTATTCGCACAACCCATCGCCTTGGGCCTACGATTAGCCGCCAACCTAACAGCAGGGCATCTTTTAATATACCTCCTCTCCACAGCGATTTGAGTCCTAGCAAGATCCCCCCTTATAGCTGGAATAACCCTCTCAATATTTTTCCTCCTATTCTTACTAGAAATAGGAGTAGCCTGCATTCAGGCCTACGTGTTTACCGCACTAGTTAAATTCTACCTTGCCCAAAACCTATAA"));
        assertTrue(0 == parser.getSequences().get(5).toString().compareTo("ATGGCCCAATCACACCCATACCACCTAGTAGACCAAAGACCCTGACCCCTCACTGGGGCTATTAGTGCCCTCATGATGACCTCCGGTCTGATACTGTGATTCCACGTAAAAAGAAACTTACTGCTAATTGCAGGAACTCTACTTCTTACCCTAACTATCCTAAAATGATGGCGAGACGTAATACGGGAAGCCACGTTTCAAGGCAGACACACCCTACCAGTTAACACTGGACTACGATACGGTATGATACTTTTTATTACCTCAGAAGTTTGCTTCTTCTTCGCTTTCTTCTGAGCATTCTTCCACAGCAGCCTAGCACCAACCATCGAACTAGGCGTATCATGACCCCCCACTGGAATAAACCCAATCAACCCCTTTCTAGTCCCTCTTCTTAACACTGCCGTCCTACTCTCATCCGGGGTAACAGTCACTTGAGCCCACCACAGAATCCTAGCCGGGAACCGCACAGAATCCATACAAGCCCTATTCCTAACTATAGCCCTAGGTATATACTTTACCACCCTACAAGCCTGAGAGTATTACGACTCCCCATTTACCATCGCCGACAGAGTGTATGGCTCTACATTTTTTGTAGCCACCGGCTTCCATGGGCTCCACGTACTCATAGGAACAACTTTCCTCTTAGTATGCTTCATACGACTAATATTCTTCCACTTTTCCAATAACCACCACTTTGGCTTCGAAGCGGCCGCCTGATACTGACACTTCGTAGACGTTGTATGACTATTCCTTTACGTCTGCATCTATTGATGAGGGTCCTAA"));
        assertTrue(0 == parser.getSequences().get(6).toString().compareTo("ATTTACAAATTTGCCATCACCCTGACATCAATCCTCCTGATAACAGGATTAATCACCACCGCAGCCCACGCCCTACCTAGCCGAAAAATAGACCTAGAAAAGTCCTCACCCTACGAATGTGGGTTTGACCCTTTGAAACACGCACGCATACCATTCTCTTTCCGATTTTTCCTGGTTGCCATCTTATTTCTCCTATTCGACCTGGAAATTGCCCTATTATTCCCGTTCCCCGTAAGAACATTCTTTATGTCCCCTAAAACCTCACTAGGACTGGCAATAGCATTCCTACTAATACTTACACTTGGGCTAGCCTTCGAATGATCTCAAGGGGGTCTCGACTGAGCCGAATAA"));
        assertTrue(0 == parser.getSequences().get(7).toString().compareTo("ATGATATATCTTATCTTAGCCTCCTCCTCAAGCCTCATCCTATCCTGGGCTGTGCCCCGAACACTACTATGACCCTCAACCATAATCTCTTCGCTTATCCTCTCCAGGCTAACTCTTAACCTAGCCCAAAAACACACATCTTCGTTATGACACAATATCTCCGCTAACTTCGCCACCGACCCTCTGGCCACACCCCTAATAATCCTAAGCATATGACTTGGCCCTTTGTCCCTTATTGCCAGAATAAAAACAATAAAGAGCCTACCACTAATAAAACAGCGCACCTTCATTTCTCTAATATTTGTTATCCTCACTGCCCTAATCCTCACGCTTGCCTCACTCGAACTAACCCTATTCTACATCGCATTTGAAACAACCCTTCTTCCAACGCTAATTCTCATATCCCGCTGGGGGGCGAAAAAGGAACGGTACCAGGCCAGTACATACTTCCTTTTCTATACTCTCGTGGGTTCCCTACCCCTACTCATAGCCATCATAAGCATTTACACCACAACTAAATCATCCCTACTACCTACTACTTCCCTTAAAAAAAAAATAACATCACTACCCACCACACTAACCTCTCTATGATGAGTCTTCTGCCTCATCGCCTTTGTAATAAAGATGCCAATTTATGGATTCCACCTTTGGCTTCCCAAGGCCCATGTAGAGGCTCCTATAGCTGGGTCAATGATCCTTGCAGCCGTACTTCTAAAGCTAGGGGGATACGGACTCACACGAACTATTACAATATTTGTTATACCCGCCTCTATAAAAATATCCTTTCCCCTCACGACCTTCTGCATATGAGGCGCCCTAATAACAAGAATAATCTGCCTCCGCCAAACAGACTTAAAGGCCTTAATAGCCTACTCATCCGTGGGCCACATGAGGTTAGTAGCTTCCGGCATATTTTCAATATCCCTATGGGGAATAAAAGGTGCCCTCACTCTCATGATTGCCCATGGATTAATATCCTCAGCCCTGTTTTGCCTTGCAAAACTCCTGTACGAACGAAACACCACACGTACCCTATCAATAACCCGAGGTTTCAAGCTAGTGACCCCACTACTCACTGTATGATGAATAATAACGTGCGTAACAAACCTAGGACTTCCCCCTACTCCTAACCTAATCGGGGAACTCCTTATCATATCCACTATTATAGACTGAAAAATCCTATCCACACCCATAATTACCACGGCAACAATATTTGGAGCCCTCTACTCCCTCCTTATATTAACTAACACCAATAAAAACTCCTTCCCAAGCTTTATTACAAAAACTCCCCCGGTAACCACTTCAGAACACACCCTAATAACCCTTCATCTTATCCCCCTAACATTCATCATACTTAAACCCTCCTACATACTAATAAAATAG"));
        assertTrue(0 == parser.getSequences().get(8).toString().compareTo("ATGGTAATAGAAATCACAAGAATAACCACAACCATAAAAATCACGATCATAACCATACTGCTTATCTCCATATTTTGCTTTCAAAGCGATCTATTGCCCCATAAGGGGCTACCTAACATCAACTTCGGAACACTTCATGACAAAAAGTTCAGGTACACACGCAAAAGAAGACTATTCTTCTCATCTATACTAAAGAGATCAGCCCTCCTTTCACTCATACCCCTCTTTTTAAAAATAAAATTTGACACCCCAGAAACAATAGTTTCCATAGCCGAATGATTACCCAAAAACGCACACCTCATAAGTATTGAACTACGATTTGACCTTTCATTCAAACTATTCTTTTCAGTTGCATTATTCGTCTCTTGATCAATCCTAGAATTCTCACACTACTACATGGCACAAGACCCAGCACCAGGCAAATTCTTCCGTCTCTTAATAATTTTCCTACTAAAAATGATCATACTCACCTCCACTAAAAACGTATTCCTTCTGTTTATAGGCTGGGAAGGGGTCGGATTCTTATCATTCTTATTGATAAGGTGGTGAACCACACGAGCAAGAGCAAAAAAATCTGCCCTCCAAGCAGTAATATACAACCGAATAGGAGACATAGGCATCCTTTTATTTTTCTCCCTGAGGGTTACAACCTTTAATTCCTGGACCCTATCAGAGATATTTGTCCTACAGCCCACAAACTCTCTAGGGAATATTTTACTAGTTTGGGCCCTCATAGCTGCTGCAGGGAAGTCCGCCCAATTCGGACTACACCCCTGACTCCCAGCAGCCATGGAGGGACCCACCCCTCTATCAGCCCTCCTCCACAGCTCTACAATGGTAGTTGCAGGAATATTCCTACTAATCCGCCGTGGCCCGGCCTATTCAGACATAAGAGCCTTTAAAACATGATGCCTGATACTTGGCTTTATCACAGCAATATTGGCTGCAACCACGGCCATATCCCAGCACGACATAAAGAAGATTGTAGCATACTCCACCACCAGACAACTAGGACTCATGATGGTCGCCATAGGCCTAAACCAACCAGAGATCGCACTATTCCACGTTTGCACACACGCATTCTTTAAGGCAATGCTATTCCTATCTTCTGGCAGAATTATCCACAGACTAAAGGACGAACAAGACATACGAAAGATGGGAGGACTCTACCTGATACTCCCTAACACAACTGCCTGCATTATTCTAGGGAGACTTGCCCTCTCTGGTACACCATTCCTTGCTGGATTCTACTCCAAGGACCTAATCCTAGAACTGGGCCTCTCTAGACTATCAAACCTAACGGGAATTATCTTAGCTCTCTTGGCAACCCTTTTGACCGCAGCTTACTCATTCCGCATTATACATTTCTGCTTCATTGGAAACCCGACCTTCTCACCGCTCTCCCCCACCAGAGAAGAAAAAACTAAACTAACCAAAGCATTAAAACGACTAGCCGCTGGTACGATAATATCAGGATGGGTAATATCAAACTTCATCCTGCAACCACCCAAAATCACCGTCAGACTCGCCCTTAAGAGTCTGGCCACCCTATTAACCGTCTCCGGGATAATATTAACAATATCACTGCTCCACACTCTTTCCCTAAAAATGCCCCCCCAAAACCTCATAAACACCAAAACGTTCACCACAAAACAATGATTCTACGAACACACGTCCCACACTATACTCACAACCTTGTCTTTCTCGCTCTCCCTAACAGGAAGGACCCAAAGAATAGACCGAGGGTGAAGAGAAAACCTGGGCGCACAAGGAATAAATCTAGCTTCTTCGGAAATCTCCCAAAAATATCAACTGGCCCAAACAGGCTACATTAAGCAATACCTTCTCCTTTCCATGTCCACCTTTACTGTCATAGCCATAATCTCCGCCGCTCTCCTATAA"));
        assertTrue(0 == parser.getSequences().get(9).toString().compareTo("ATGGTTTTTTATATTGTATTGGTTGTTATGCTTTTGGGAAGTACTCTGGTGTTTTATAGGTTGTCCCCTTATTTTGGTGCCCTAGGCCTGGTTTTAGTGGCCGTGTCGGGGTGTGTCCTTTGCGGGTTTCTTGGGTCCTCTTTTGTGGCTCTGGTGCTTGTTCTTATTTATATGGGGGGAATGCTGGTTGTTTTTGTTTATTCTACGGCTATTTCTGCAGAGCGGTACCCAGTAGTGAGTAATTTGAATGAGGTGTTTCTCTTAAGGGGCGTGGGCGTTGTTTGGGTTTTTGTCAGGTTTGATCCTTTCTTGGGCTTTTCGCTCGGGGGGTGGTCTGTGGTAGTAGAAAGGGATCTTGTTGGGGGTGGTACTTTGTATAAAGTATTGGGGGGTTATTTGTTAGTAGGGGGGTTTGTATTCTTGTTTGCTTTAGTAGTTGCACTAGTGATTACTTATGGGGCGGAATATAAAATATTGAAGGCCCTGTAG"));
        assertTrue(0 == parser.getSequences().get(10).toString().compareTo("ATGAACCTGAAACTAAAAAACATATTCGGCCAATTCTCCCCGGACATCGTAGCCCTAATTCCCATGACCCTCATCGCCTTGGCCATTAACCTAGGATGACTCTCTCTAACAAGAAGAACAAACTGACTCCTCACCCGAAGTGGAACTTTGATCCAAACCTTCAAGCAGGGAACTCTAAGAATACTATTCCAGCAAGCCAACCCCAAAATTGCACCCTGAATCCCAGCCTTTACCTCTGTATTTATATTCATCCTATCAGTAAAAGTATTAGGACTACTACCGTATGCCTTCACTTCTACCAGCCACATATCACTTACTTACTCCATAGGCATTCCCTTCTGAATGTCCGTCAACGTCCTTGGATTCTATCTAGCATTCAAAAGTCGACTAAGACACCTAGTACCCCAAGGTACTCCCCCCTACCTAATCCCCCTGATGGTAATAATAGAAACAATCAGACTATTCGCACAACCCATCGCCTTGGGCCTACGATTAGCCGCCAACCTAACAGCAGGGCATCTTTTAATATACCTCCTCTCCACAGCGATTTGAGTCCTAGCAAGATCCCCCCTTATAGCTGGAATAACCCTCTCAATATTTTTCCTCCTATTCTTACTAGAAATAGGAGTAGCCTGCATTCAGGCCTACGTGTTTACCGCACTAGTTAAATTCTACCTTGCCCAAAACCTATAAAAAATGAACCTGAAACTAAAAAACATATTCGGCCAATTCTCCCCGGACATCGTAGCCCTAATTCCCATGACCCTCATCGCCTTGGCCATTAACCTAGGATGACTCTCTCTAACAAGAAGAACAAACTGACTCCTCACCCGAAGTGGAACTTTGATCCAAACCTTCAAGCAGGGAACTCTAAGAATACTATTCCAGCAAGCCAACCCCAAAATTGCACCCTGAATCCCAGCCTTTACCTCTGTATTTATATTCATCCTATCAGTAAAAGTATTAGGACTACTACCGTATGCCTTCACTTCTACCAGCCACATATCACTTACTTACTCCATAGGCATTCCCTTCTGAATGTCCGTCAACGTCCTTGGATTCTATCTAGCATTCAAAAGTCGACTAAGACACCTAGTACCCCAAGGTACTCCCCCCTACCTAATCCCCCTGATGGTAATAATAGAAACAATCAGACTATTCGCACAACCCATCGCCTTGGGCCTACGATTAGCCGCCAACCTAACAGCAGGGCATCTTTTAATATACCTCCTCTCCACAGCGATTTGAGTCCTAGCAAGATCCCCCCTTATAGCTGGAATAACCCTCTCAATATTTTTCCTCCTATTCTTACTAGAAATAGGAGTAGCCTGCATTCAGGCCTACGTGTTTACCGCACTAGTTAAATTCTACCTTGCCCAAAACCTATAA"));
    }

}