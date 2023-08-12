package com.filmdoms.community.article.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
class ArticleServiceTest2 {


    @Test
    public void regexTest(){

        String content = """
                
                <p class="ql-align-center"><strong class="ql-size-large">소멸로 쌓아 올린 세계를 만나다</strong></p>
                <p>&nbsp; </p>
                <p class="ql-align-right">에디터 권영은</p>
                <p class="ql-align-right"> </p>
                <p class="ql-align-justify"><img src="https://api.filmdoms.studio/image/63fe39ac-2cdd-4571-99f5-38a92f9db15e.png"></p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify"> “이해가 안 돼.” ‘오사코’의 뾰로통한 입술과 의구심 가득한 눈은 쉴 틈이 없다. 그는 이 세계가 영 이해되지 않는다. 가고시마를 관조하듯 떡하니 자리한 사쿠라지마 화산은 조용할 날이 없다. 화산재가 풀풀 날리는데 사람들은 아무도 신경 쓰지 않는다. 이뿐만이 아니다. 그가 오사카에서 함께하던 가족과 흩어진 지 6개월째다. 그러나 자신을 제외한 그 누구도 이 소멸의 소용돌이에 조바심을 내지 않는다. 오사코는 제 생각대로 돌아가지 않는 이 세상을 고치려 애쓴다. 화산과 가족, 이 두 가지 문제를 단번에 해결할 방법은 기적을 찾는 것. 그는 새로 개통하는 두 개의 신칸센이 마주하는 순간을 뒤쫓는다. 놀랍게도 그는 신칸센이 서로 스치는 순간을 목도한 뒤 세계를 긍정할 수 있게 된다. 과연 그 접촉의 찰나는 자라나는 아이에게 무엇을 가져다주었을까.</p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> <strong class="ql-size-large">세계와 시간 그리고 만남</strong></p>
                <p class="ql-align-justify">&nbsp; <img src="http://api.filmdoms.studio/image/e93a773e-1e98-4abf-8552-0558afdaf561.jpg"></p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">사쿠라지마 화산은 활발히 운동하며 매일 다른 양의 화산재를 뿜어낸다. 이 변칙적인 존재는 가고시마의 동서남북 어디에서나 보인다. 마을 사람은 화산과 함께하는 삶을 받아들이며 살아간다. 화산은 하나의 장소가 아닌 삶을 둘러싸고 있는 조건이자 세계와 같다. 이 세상은 자신이 살아있음을 증명하듯 매일 움직인다. 이 움직임은 누구도 예측할 수 없으며 통제할 수 없는 우연성으로 가득하다.</p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">이 거대하고 불규칙한 세계(화산) 속에 질서를 부여하는 것은 다름 아닌 ‘시간’이다. 일상은 반복되어 나타난다. 해가 뜨면 방을 정리하고 식사로 배를 채운 뒤 문밖을 나서며 밤 끝자락엔 잠을 청한다. 이러한 일련의 몸짓은 현재의 시간을 앞으로 밀고 나가는 행위이다. 이 순행의 흐름은 때때로 과거와 미래를 호출하며 변주된다. 가령 가루칸으로 희미해진 과거의 전통을 불러내려 한다든가 오사코가 화산이라는 미래의 사건을 호출해 기적을 바라는 일이 그러하다. 이러한 시간의 교직은 거센 에너지로 질주하는 두 존재의 운동, 바로 오사코가 애타게 기대하는 두 기차의 만남과 닮아있다.</p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> 아이들은 ‘가고시마에서 사쿠라 호(A)가, 하카타에서 츠보미마 호(B)가 각각 260km로 달리다가 스치는 순간’을 기다린다. 기차가 마주하는 순간은 기적이 일어날지도 모르는 가능성을 내포한다. 이는 서로 다른 두 방향의 움직임을 전제한다. A 기차는 왼쪽에서 오른쪽으로 순행하고 B 기차는 오른쪽에서 왼쪽으로 역행한다. A는 시간의 흐름을 따르는 생성의 운동이며 B는 시간이 존재하지 않는 무의 공간으로 나아가는 소멸의 운동이다. 이 두 생성과 소멸이 마주하며 작은 틈이 생기는 순간 만남이 이뤄진다. 모든 존재는 자신이 몰랐던 타자의 영역에 접속하며 저마다의 방식으로 거대한 세계를 촘촘히 채워간다.</p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">&nbsp;</p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"><strong class="ql-size-large"> 류와 오사코, 그리고 할아버지</strong></p>
                <p class="ql-align-justify">&nbsp; <img src="https://api.filmdoms.studio/image/cd8f8b04-8993-4f81-b45f-0be3ac57ac3f.png"></p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">시간의 흐름에 몸을 맡기는, 즉 현재에 가장 충실한 인물은 ‘류’이다. 그는 아빠와 단둘이 사는 지금이 제법 마음에 든다. 하루가 멀다 않고 싸우던 부모의 모습은 돌아가기 싫은 과거일 뿐. 그는 마당의 텃밭에 채소를 심고 미래에 자라날 새싹을 틔우느라 여념이 없다. 류는 앞으로 나아갈 미래를 꿈꾼다. 하지만 오사코는 다르다. 그는 자신과 단절된 가족의 이미지를 계속해서 불러낸다. 그의 시선이 닿는 곳은 유리 액자 너머에 있는 자신과 류의 행복한 한때, 모자(母姊)가 따스한 눈짓을 주고받는 찰나 그리고 기차역에서 재회한 4인 가족의 모습이다. 낯선 이의 순간이 모여 하나의 가족을 이루듯 그는 뿔뿔이 흩어진 자기 가족도 끌어모으길 바란다.</p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> 오사코는 현재를 부정하고 가족의 소멸을 의도적으로 멈추려 애쓰며 재회의 순간만을 기다린다. 다시 말해 그는 가족의 세계를 벗어나지 못한다. 자신과 분리된 가족을 향한 열망은 버스 창을 열고 달리는 행위에서 단적으로 드러난다. 오사코는 감기에 걸릴 위험을 감수하더라도 자기 얼굴에 스치는 기분 좋은 바람을 느끼길 원한다. 이는 고통을 수반할지라도 한집에 살고픈 그의 간절한 염원을 담고 있다. 그러므로 그의 소원은 사쿠라지마 화산이 폭발하는 재난을 빚더라도 가족과 재회할 수 있는 방향으로 흘러간다. 그는 화산을 자신만의 것으로 만들며 분화가 초래할 고통을 축소 시킨다. 오사코는 실재하는 사쿠라지마 화산을 바라보고 두 손을 맞대는 것이 아니라 자신이 그려낸 화산을 보고 소원을 빈다. 그러나 그림 속엔 죽음을 맞이할 가고시마의 시민과 가족이 재회하길 원치 않는 류 그리고 속수무책으로 도망치게 될지도 모를 친구들이 없다. 오로지 가족의 재회에 기뻐하는 오사코만 있다.</p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> &nbsp; </p>
                <p class="ql-align-justify"> 할아버지는 이 두 아이의 반대 영역에 서 있다. 그는 시간의 역행에 몸을 맡기며 과거에 자리한 것들을 지키려 애쓴다. 그의 친구들은 신규 개통하는 사쿠라 호의 인기에 힘입어 분홍색 가루칸을 만���어보라고 말하나 할아버지는 단호히 거절한다. 그에게는 가족보다 중요한 조상과 전통이 있기 때문이다. 이러한 그의 양태는 가루칸 작업실에 걸린 정지된 화산 사진과 닮아있다. 아이들과 달리 그는 소멸을 명확히 인식하는 존재이기도 하다. 교차로에서 아이들은 할머니가 순식간에 사라지는 모습을 목격한다. 오사코의 친구는 자신의 아랫도리를 잡고 말한다. “여기가 좀 찌릿찌릿하지 않아?” 할머니는 그저 물건을 찾으려 걸음을 돌렸을 것이다. 그러나 아이들에게 그녀는 순식간에 공기 중으로 흩어진 불가해한 존재가 된다. 그들은 소멸의 신호만 어렴풋이 느낄 뿐 아직 그에 대해 명명하지 못한다. 하지만 육체의 사라짐을 앞둔 할아버지의 태도는 사뭇 다르다. 할아버지와 그의 친구 역시 가슴의 저릿함을 느낀다. 이는 점차로 가까워지는 무의 상태, 죽음으로 향하는 신호다. 어른들은 자신의 앞에 소멸로 나아가는 긴 선로가 놓여 있음을 알고 있다. </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"><strong class="ql-size-large">두 개의 낯선 목소리를 듣다 </strong></p>
                <p class="ql-align-justify">&nbsp; <img src="https://api.filmdoms.studio/image/0972f0ce-ab25-4c58-a8f3-20813d5cc987.jpg"></p>
                <p class="ql-align-justify"> 할아버지와의 만남은 오사코의 세계에 균열을 내기 시작한다. 화장실에서 오사코는 화산재가 그득한 수영복을 빨며 이해가 되지 않는다며 연신 되뇐다. 그는 화장실 거울 안에 갇혀 자기의 세계에 몰두하고 있다. 그 순간 화장실 문턱에 할아버지가 나타난다. 어리둥절한 오사코는 화산을 바라보듯 문간을 통해 그와 마주한다. 오사코는 아직은 알 수 없는 생소한 존재인 할아버지의 뒤를 졸졸 따라다니기 시작한다. 대관람차에서 오사코는 할아버지에게 불만을 토로한다. 그에 반해 할아버지는 화산을 순순히 받아들인다. 그는 모름지기 살아있는 존재는 운동할 수밖에 없으며 그 움직임은 타자가 통제할 수 없다고 여긴다. 이처럼 할아버지는 이미 화산이 대변하는 세계의 원리를 깨달은 존재다. 그래서인지 그는 왕왕 화산의 모습을 하고 있다. 예컨대 할아버지는 항상 의자에 앉아 제자리를 지킨다. 자리를 뜨더라도 그가 앉았던 의자는 남아있다. 매일 담배를 피우며 연기를 내뿜는 것도 잊지 않는다. 마치 화산이 지나간 자리에 수북이 쌓이는 화산재처럼. 이런 할아버지의 응답은 오사코의 세계를 침투하는 첫 번째 목소리다. 오사코는 할아버지의 말을 즉각적으로 깨닫지 못한다. 대신 그의 얼굴에는 호기심 가득한 표정이 피어오르기 시작한다. </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">할아버지와 함께하며 서서히 벌어진 균열에는 희망이 흘러든다. 바로 새로운 가족 구성의 가능성이다. 할아버지와 오사코가 밀담을 주고받는 순간, 카메라는 그들을 창 바깥에서 지켜본다. 대관람차와 가루칸 작업실에서 그들은 함께 있되 문과 창 사이에서 분리돼있었다. 하지만 이제는 가지런히 하나가 되어 머문다. 대망의 비밀 작전 수행 날, 할아버지와 오사코의 관계는 뒤집힌다. 할아버지는 먼저 학교에 도착한 오사코를 뒤따라 도착하고 그는 자신보다 먼저 교문을 나가는 손자를 뒤에서 바라본다. 도움을 주고받는 이 관계는 오사코의 여정에 샛길을 낸다. 그가 엄마, 아빠, 류가 아닌 다른 존재와 소통할 수 있음을 몸소 확인한 것이다.</p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">이제 오사코는 구마모토에 도착한다. 그가 사쿠라지마 화산처럼 높은 산에 감탄을 내뱉자 외화면에서 낯선 목소리가 다시 한번 침투한다. “그건 후겐산이야. 벌써 20년도 지난 일이지. 분화가 있었지. 화산재가 쏟아져서 50명이나 죽었지. 두 번 다시 그런 불행은 없어야지.” 오사코와 친구들, 셋뿐이던 세상은 시선을 돌려 기관장에게로 초점을 옮긴다. 그 순간 세 아이는 사쿠라지마 화산처럼 흐릿한 배경이 된다. 이 시선의 이동은 오사코의 세상에 낯선 존재들을 들여온다. 사쿠라지마 화산 폭발은 기적인 동시에 타인이 다칠 가능성을 내포한 불확실한 사건이다. 반면에 이미 일어난 사건은 막연히 가정해보는 미래의 일이 진짜 ‘재난’이 될 수 있음을 알린다. 할아버지와의 관계에서 생긴 균열은 더욱 깊어진다. 오사코의 말��� 얼굴은 자신을 초과하는 세계를 만난 생경함과 두려움으로 채워진다. 어쩐지 그는 자신의 바깥에서 들려오는 수런거림을 외면할 수 없다. </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">&nbsp;<img src="https://api.filmdoms.studio/image/5cd3e062-7757-4b37-bac0-e50e4b5b321f.jpg"> </p>
                <p class="ql-align-justify">험난한 여정을 거쳐 도착한 두 기차의 만남 앞에서 오사코는 두 번째 가정을 마주한다. 기차가 접촉하는 순간 발생하는 엄청난 에너지는 미래와 과거의 시간을 현재로 이끈다. 이 두 시제의 만남은 시간이 뒤섞이는 유일한 순간이다. 첫 번째 가정 속에서 오사코는 자기 가족 이외에 어떤 것도 바라보지 못했다. 그러나 두 번째 가정 속에선 화산이 폭발한다면 사라질지도 모를 사사롭고 따스한 순간을 목격한다. 잉여처럼 보이는 찰나들은 무수히 많은 익명의 염원으로 이어진다. 화산이 내포한 소멸의 가능성과 일상이 축적된 지난 순간들은 서로 손을 맞붙잡는다. 오사코는 깨닫는다. 그 자신만의 화산을 넘어 사쿠라지마 화산까지 폭발한다면 모두의 희망으로 가득한 삶이 무너지고 말 것을. 오사코는 첫 번째 가정을 내뱉지 않고 종식시킨다. 가족의 해체를 그토록 거부했던 그는 스스로 소멸을 택한다. 그의 얼굴에는 낯선 문을 열어젖혀 낯익은 오늘에 도착한, 기어이 세계에 당도한 이의 조용한 환희가 일렁인다.</p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> 이는 비단 오사코만의 이야기는 아니다. 기차와의 접촉에도 불구하고 마블의 소생은 이뤄지지 않는다. 아이들은 집으로 향하기 전 죽음의 흔적이자 시간의 축적으로 완성된 묘 앞에 머문다. 온통 생의 기���으로 가득했던 그들에게 정적인 시간이 불쑥 다가온다. 아이들은 그 앞에서 담담하다. 이제 그들은 사라지는 것이 있음을 안다. </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"><strong class="ql-size-large">사라짐으로써 완성되는 오늘</strong></p>
                <p class="ql-align-justify">&nbsp; <img src="https://api.filmdoms.studio/image/4a0c34ba-c9ed-4d2b-bd07-b99409e83753.png"></p>
                <p class="ql-align-justify"><br></p>
                <p class="ql-align-justify">오사코는 가족의 세계를 벗어나 혼자서 설 수 있게 된다. 각자의 집으로 돌아가는 선로에서 그는 류에게 세계를 선택했음을 고백한다. 그는 이제 자신과 다른 개별의 존재로서 류가 있음을 안다. 두 기차가 마주한 이후에 각자 제 길을 갔듯 오사코는 후쿠오카의 세계(친구)로 향하는 류를 말없이 보내준다. 구마모토로 오는 길에 온통 신칸센이 보이는 선로를 찾던 그의 시선은 이제 바깥의 풍경으로 흘러 들어간다. 가고시마는 더는 가족의 재회로 향하는 경유지가 아니다. 새로운 가족이 자신을 기다리고 있는 목적지에 가깝다. 오사코와 친구들이 가고시마에 도착해 경쾌한 발걸음으로 기차역을 내려갈 때 카메라는 거리를 두고 그곳을 조망한다. 가고시마를 떠날 땐 보이지 않던 주변 세상은 이제야 제 존재를 드러낸다. 인파 속에 섞인 아이들은 이름 모를 많은 이들처럼 서로 스쳐 지나간다. 화면 위를 가득 채우는 교차의 운동은 세계를 촘촘히 메꿔 간다.</p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> 오사코의 돌아가는 가벼운 발짓에는 이 세계를 향한 긍정이 담겨있다. 가고시마 집을 탈출하려는 여정은 ���났다. 항상 문밖으로 뛰쳐나가던 그는 자신을 향해 활짝 열린 문을 매끄럽게 넘어선다. 이제 오사코는 할아버지처럼 손을 내민다. 낯선 존재를 어루만지듯 화산이 뿜어낸 오늘의 화산재를 가늠한다. 일련의 여정 끝에 그는 자신을 둘러싼 이 세계를 운용할 수 없음을 받아들인다. 그는 그저 매일 다른 운동을 느끼기로 한다. 오사코는 이제 가고시마 풍경의 일부다. </p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify"><img src="https://api.filmdoms.studio/image/940f03e2-27dc-4418-80d6-0cbd5ad0abb7.jpg"></p>
                <p class="ql-align-justify"> </p>
                <p class="ql-align-justify">오사코와 류가 등을 맞대고 키를 재던 장면을 떠올려보자. 그들이 자리에서 일어서자 카메라는 얼굴이 아닌 땅에 붙은 두 발을 먼저 비춘다. 그 후 높낮이가 다른 두 머리를 보여준다. 이 두 쇼트는 저마다의 성장의 속도가 다를 뿐 모든 이들이 같은 세계에 발 딛고 있음을 보여준다. 키와 같이 세계를 배우는 속도도 다르긴 매한가지다. 사라진 할머니를 보고 이유 모를 찌릿함을 느끼던 아이들은 묘 앞에 멈춰 서며 죽음의 순간을 지각한다. 이에 반해 류와 친구들은 그곳을 웃으며 지나친다. 류는 오사코가 그랬듯이 아빠에게 “세계가 무엇이냐?” 묻지만 그는 답을 얻지 못한다. 우리는 마냥 해맑은 류의 얼굴을 보면서 어렴풋이 예감한다. 오사코가 할아버지를 따르며 배웠듯 류도 자신보다 앞선 시간을 산 이를 통해서 세계를 배우게 될 것을.</p>
                <p class="ql-align-justify">&nbsp; </p>
                <p class="ql-align-justify"> &nbsp; </p>
                <p class="ql-align-justify">사람이 태어나면 이 땅을 누비면서 살아가지만 어떤 곳인지는 미처 알지 못한다. 어른이 되면 마법같이 진리를 깨닫는 것도 아니다. 그저 몇 배의 시간을 살��� 무수한 생성과 소멸 혹은 만남과 이별의 순간을 쌓을 뿐이다. 어른들이 아이들의 얕은수를 금방 알아내고 같은 집에 고작 6개월 살면서 그들의 의중을 읽어내는 것도 이 때문에 가능한 것이리라. 거대한 세계의 질서는 사람들에게 무심하다. 그 안에서 시간을 타고 분주히 운동하는 존재들은 간간이 만나 새로운 것을 만들어낸다. 존재하는지 몰랐던 세계의 문을 여는 일은 연쇄된 접촉으로 시작된다. 이것이 아이들이 깨닫게 되는 기적이다. 갓 태어난 이들은 앞선 존재의 뒤를 밟으며 바지런히 선로를 쌓아 올리고, 시간이 흐르면 어른들은 선로의 끝으로 향할 것이다. 그 사이에서 아이들은 마침내 소멸과 생성의 선로가 나란히 이어진 오늘을 마주하리라. </p>
                <p>&nbsp; </p>
                <p><br></p>
                <p><br></p>
                <p><br></p>
                <p class="ql-align-right"><strong>Written by 권영은</strong></p>
                <p>&nbsp; </p>﻿ |
                """;

        String patternString = "<img src=\\\"(https?:\\/\\/[a-z./0-9-]*)\\\">";

        // 1) 문자열 형태의 정규표현식 문법을 정규식 패턴으로 변환
        Pattern pattern = Pattern.compile(patternString);

        // 2) 패턴 객체로 matcher 메서드를 통해 문자열을 검사하고 필터링된 결과를 매처 객체로 반환

        Matcher matcher = pattern.matcher(content);
        matcher.results().forEach(matchResult -> System.out.println(matchResult.group(1)));
        matcher = pattern.matcher(content);
        System.out.println(matcher.results().count());



    }

}