package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@SpringBootTest
public class OpenAiVectorStoreTests {
    private static final Logger log = LoggerFactory.getLogger(OpenAiVectorStoreTests.class);

    @Autowired
    private PgVectorStore vectorStore;

    @Autowired
    private OpenAiChatModel chatModel;

    private String news1 = """
            오픈AI는 실시간으로 대화 상대를 보고 그 사람의 말을 들으면서 자연스럽게 대화하는 챗GPT를 출시했다고 12일(현지시간) 밝혔다. 지난 5월 듣고 말하는 기능을 처음 공개한 지 7개월 만이다. 지난 9월에는 한층 더 자연스러운 대화가 가능한 챗GPT의 고급 음성 모드를 출시한 바 있다. 이 음성 모드는 때론 활발하거나 정중한 톤의 목소리를 골라서 대화할 수 있게 한 기능이다.
            여기에서 한 발 더 나아가 이제는 챗GPT가 '시각'을 갖게 된 것이다. 그렉 브로크만 오픈AI 회장 겸 사장을 포함한 임직원은 이날 챗GPT 새 기능을 시연하는 영상도 공개했다. 이 영상에서 챗GPT는 사슴 뿔을 쓰고 있는 직원, 산타 모자를 쓰고 있는 직원들과 인사를 한 후, 누가 산타 모자를 쓰고 있냐는 물음에 정확히 이름을 말했다.
            또 카메라로 커피를 내리는 드리퍼와 주전자 등 커피 세트를 보여주자 "커피 드리퍼와 주전자가 보이네요. 커피를 내릴 계획인가요?라고 물었다. 커피 내리는 모습을 보여주자, 챗GPT는 단계별로 커피를 맛있게 내리는 방법을 설명하는 모습도 보였다. 실시간으로 상황을 보여주면서 도움을 요청하고 답변을 받을 수 있었다.
            새로운 기능을 사용하려면, 채팅창 옆에 있는 '음성 아이콘'을 누르고 화면 왼쪽 하단의 '비디오 아이콘'을 누르면 된다. 챗GPT플러스와 팀, 프로 등 유료 서비스를 구독 중인 사용자는 이날부터 이용 가능하다. 다만 기업용인 챗GPT엔터프라이즈와 교육용인 에듀 가입자는 1월 이후 이 기능을 사용할 수 있다. 유럽연합(EU)과 스위스, 아이슬란드 등 유럽 일부 국가에 대한 출시 일정도 정해지지 않았다. \s
            오픈AI는 크리스마스를 기념해 챗GPT 고급 음성 모드에 '산타 음성'도 추가했다. 채팅창 옆 눈송이 모양 아이콘을 눌러 산타 목소리를 선택하면 산타 AI와 대화할 수 있다. 산타 음성은 내년 1월 초까지 제공된다.
            한편, 오픈AI는 전날 발생한 챗GPT와 소라의 접속 장애 문제에 대해서도 사과했다. 챗GPT는 전날 오전 8시 17분부터 오후 12시 38분까지 로그인, 사용 등이 원활하지 않았던 것으로 파악됐다. 오픈AI는 공식 X(옛 트위터) 계정을 통해 전날 오전 9시 15분에 문제를 확인했다고 밝힌 후 같은 날 오후 2시쯤 서비스가 복구됐다고 알렸다. 다만 장애 원인을 설명하진 않았다.
            """;
    private String news2 = """
            삼성전자는 구글, 퀄컴과 12일(현지 시간) 미국 뉴욕 구글 캠퍼스에서 개발자 대상 ‘XR 언락’ 행사를 개최하고 ‘안드로이드 XR’ 플랫폼과 이를 탑재한 ‘프로젝트 무한(無限)’을 소개했다. 메타, 애플에 이어 삼성·구글·퀄컴 동맹도 확장현실(XR) 시장에 본격 참전하며 내년 XR 경쟁도 치열해질 전망이다.
            안드로이드 XR은 삼성전자, 구글, 퀄컴 등 3사가 공동 개발했다. 구글의 생성형 AI 모델인 제미나이가 지원돼 기기에서 자연스러운 대화, 정보 검색이 가능하다. 안드로이드 XR은 개방형 생태계인 만큼 구글, 삼성전자뿐만 아니라 타사(서드파티) 애플리케이션(앱), 서비스도 호환된다.
            프로젝트 무한은 안드로이드 XR이 적용되는 최초 헤드셋으로 내년 출시 예정이다. 삼성전자는 “무한이라는 이름 그대로 물리적 한계를 초월한 공간에서 몰입감 넘치는 경험을 제공하겠다는 의미”라고 설명했다.
            블룸버그통신에 따르면 삼성전자와 구글은 최근 일부 언론 매체를 대상으로 XR 헤드셋을 공개하고 시연했다. 블룸버그는 삼성전자 XR 헤드셋이 애플 XR 기기인 비전 프로 대비 가볍고 장시간 착용해도 편안하다고 보도했다. 가격도 애플 비전프로의 3499달러보다 낮을 전망이다. 삼성전자는 XR 헤드셋뿐만 아니라 안경 형태에 가까운 글래스 제품 출시도 준비중인 것으로 알려졌다.
            최원준 삼성전자 MX 사업부 개발실장(부사장)은 이날 행사에서 “끊임없이 확장되는 생태계와 폭넓은 콘텐츠를 바탕으로 사용자에게 풍요로운 경험을 제공하겠다”라고 했다. 김기환 삼성전자 MX 사업부 이머시브 솔루션 개발팀 부사장은 “안드로이드 XR 플랫폼을 통해 선보일 ‘프로젝트 무한’ 헤드셋은 가장 편안하고 인체공학적인 디자인으로 비교할 수 없는 경험을 제공하도록 설계했다”고 했다.
            샤흐람 이자디 구글 AR부문 부사장은 “XR 헤드셋을 통해 사용자는 가상, 현실 세계를 자유롭게 넘나들 수 있다”며 “유튜브, 구글TV, 구글 포토, 구글 맵스 등 구글 인기 앱들도 헤드셋에 맞춰 새롭게 재탄생한다. 안드로이드 기반이기 때문에 구글플레이의 모바일 및 태블릿 앱을 그대로 사용할 수 있다”고 했다. 아울러 구글의 AI 검색 툴인 ‘서클 투 서치’도 지원되고 XR에 특화된 다양한 앱, 게임 등 콘텐츠를 출시할 계획이다.
            구글은 소규모 그룹 대상으로 안드로이드 XR 기반 글래스 제품 테스트를 조만간 진행한다고 밝혔다. 제미나이가 지원되고 XR로 보여주는 화면상 길 찾기, 번역하기, 메시지 요약 등 기능을 수행할 수 있는 제품이다.
            앞서 삼성전자, 구글, 퀄컴은 지난해 2월 파트너십을 체결하고 XR 생태계 구축에 나선다고 발표한 바 있다. 올 10월엔 노태문 삼성전자 MX 사업부장(사장)이 퀄컴 스냅드래곤 개발자 행사에서 “이제 AI의 이점을 혁신적인 XR 생태계를 통해 확인할 때”라며 출시가 임박했다는 사실을 예고하기도 했다.
            삼성전자와 구글이 XR 헤드셋 ‘프로젝트 무한’의 내년 출시를 기정사실화 하며 메타의 퀘스트, 애플의 애플비전와의 치열한 3파전이 예상된다. 또 메타도 9월 개발자 행사에서 ‘오라이언’이라는 스마트 안경을 공개하며 다양한 폼팩터 경쟁이 벌어질 것으로 전망된다.
            """;
    private String news3 = """
            도널드 트럼프 미국 대통령 당선인이 우크라이나 전쟁의 휴전과 전후 상황을 관리할 방안으로 '유럽 군대의 주둔'을 언급했다고 월스트리트저널(WSJ)이 현지시간 12일 보도했습니다.
            트럼프 당선인의 취임이 다가오면서 '조기 종전 구상'이 윤곽을 드러내기 시작하는 것으로 보이지만, 이를 실현하기까지는 많은 난관이 남아 있습니다.
            WSJ에 따르면 트럼프 당선인은 지난 7일 프랑스 파리 엘리제궁에서 이뤄진 볼로디미르 젤렌스키 우크라이나 대통령, 에마뉘엘 마크롱 프랑스 대통령과의 3자 회동에서 "우크라이나의 북대서양조약기구(NATO·나토) 가입을 지지하지 않지만, 전쟁이 멈춘 이후에는 강하고 잘 무장된 우크라이나가 등장하는 모습을 보고 싶다"고 밝혔습니다.
            그러면서 우크라이나의 방어와 지원에 유럽이 주된 역할을 맡아야 하고, 유럽의 군대가 우크라이나에 주둔하며 휴전 상황을 감시하기를 원한다고 말했습니다.
            당시 회담 내용을 아는 관계자들에 따르면 트럼프 당선인은 휴전 협정에 대한 미국의 지원 가능성은 열어뒀지만 미군의 개입은 배제했습니다.
            아울러 중국이 러시아에 전쟁 종식을 설득할 수 있도록 유럽이 더 많은 행동에 나서야 한다고 주장했고, 중국이 동의하지 않을 경우 관세 부과를 협상 카드로 활용하는 방안을 논의한 것으로 전해졌습니다.
            트럼프 당선인은 대통령 선거 과정에서 "취임하면 24시간 안에 우크라이나 전쟁을 끝낼 수 있다"고 공언해 왔으나 당선 이후에는 추진 방안을 명시적으로 밝히지 않았습니다.
            대신 측근들을 중심으로 우크라이나의 나토 가입은 보류하고 현 전선을 동결시키는 형태의 휴전 방안이 거론돼 왔습니다.
            트럼프 당선인이 언급한 '유럽군의 우크라이나 주둔'은 이런 휴전안을 보다 구체화한 것으로 해석됩니다.
            관계자들에 따르면 우크라이나에 배치되는 유럽군은 나토와 무관한 평화유지군이나 휴전 감시군의 일부가 될 것이라고 WSJ은 전했습니다.
            다만 각론으로 들어가면 여전히 불확실성이 큽니다.
            우크라이나 주둔군에 유럽의 어느 나라가 참여할지, 병력 규모는 얼마나 될지, 미국은 어떻게 휴전협정을 지원할지, 러시아가 이를 수용할지 등이 난제가 될 수 있습니다.
            유럽 국가들은 우크라이나에 병력을 보냈다가 자칫 러시아가 휴전 협정을 위반할 경우 이에 맞서게 되는 상황을 경계합니다.
            러시아가 휴전을 받아들이더라도 이후 병력을 재건해 우크라이나를 재침공할 수 있다는 우려도 큽니다.
            앞서 서방의 우크라이나 파병론을 제기했던 마크롱 대통령은 이날 폴란드를 방문해 평화유지군 파병을 논의했지만, 도날트 투스크 폴란드 총리는 기자회견에서 우크라이나에 파병할 계획이 없다고 일축했습니다.
            이에 프랑스 관리들은 유럽군의 주둔에는 미국의 지원이 함께 이뤄져야 한다는 입장이지만, 트럼프 정부가 이를 받아들일지는 분명하지 않다고 WSJ은 지적했습니다.
            러시아 역시 나토 가입국의 병력이 우크라이나에 주둔하는 것을 받아들이지 않을 수 있습니다.
            블라디미르 푸틴 러시아 대통령은 올해 초 나토 병력의 우크라이나 파병에 대해 "핵전쟁의 실질적 위협을 키울 것"이라고 말했습니다.
            WSJ은 "측근들에 따르면 트럼프 당선인은 아직 우크라이나에 대해 깊이 고민하거나 특정한 계획을 고집하고 있지 않다"며 "국가안보팀이 구성되고, 동맹국들은 물론 잠정적으로는 푸틴 대통령과도 대화한 뒤에 핵심적인 결정이 이뤄질 것"이라고 전망했습니다.
            """;

    @Test
    public void testVectorStore() {
        List<Document> documents = List.of(
                new Document(news1),
                new Document(news2),
                new Document(news3)
        );
        vectorStore.write(documents);
    }

    @Test
    public void testTextReader() {
        DocumentReader reader = new TextReader("classpath:/운수좋은날.txt");
        // TextReader reader = new TextReader("file:/C:/temp/sample.txt");
        // TextReader reader = new TextReader("file:/home/user/sample.txt");
        // TextReader reader = new TextReader("https://example.com/data/sample.txt");
        // TextReader는 기본적으로 하나의 Document를 만들지만 이것을 List<Document>로 만드는 것은 RAG 구조를 염두에 두고 만들었기 때문
        // log.info("{}", reader.read().getFirst().getText());

        List<Document> documents = reader.read();
        documents.forEach(document -> document.getMetadata().put("category", "소설"));
        TokenTextSplitter splitter = new TokenTextSplitter();
        vectorStore.write(splitter.split(documents));
    }

    @Test
    public void testPdfReader() throws IOException {
        DocumentReader reader = new PagePdfDocumentReader("classpath:/인공지능_시대의_예술.pdf");
        List<Document> documents = reader.read();
        documents.forEach(document -> document.getMetadata().put("article", "ai")); // ai, avata
        TokenTextSplitter splitter = new TokenTextSplitter();
        vectorStore.write(splitter.split(documents));
    }

    @Test
    public void testSimilaritySearch() {
        String question = "김첨지 아내는 무슨 병에 걸렸나요?";
        //String question = "인공지능을 사용하면 일반 사람들도 예술가가 될 수 있을까?";

        // Simple format
        //List<Document> documents = vectorStore.similaritySearch(question);

        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                .query(question)
                .topK(3)
                //.filterExpression("article == 'ai'").build());
                .filterExpression("source == '운수좋은날.txt'").build()
        );

        var information = String.join("\n", documents.stream().map(Document::getText).toList());

        String prompt = MessageFormat.format("""
                다음의 정보를 기반으로 하여 답을 하고, 정보가 없는 경우에는 모른다고 답변 하세요.
                [정보]
                {0}
                [질문]
                {1}
                """, information, question);

        Message message = new UserMessage(prompt);
        String result = chatModel.call(message);

        log.info("{}", result);
    }
}
