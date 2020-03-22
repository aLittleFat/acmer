package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import cn.edu.scau.acm.acmer.repository.OjAccountRepository;
import cn.edu.scau.acm.acmer.repository.ProblemRepository;
import cn.edu.scau.acm.acmer.service.BzojService;
import cn.edu.scau.acm.acmer.service.ProblemService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BzojServiceImpl implements BzojService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OjAccountRepository ojAccountRepository;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ProblemRepository problemRepository;

    @Override
    public void bzojLogout() {
        String url = "http://www.lydsy.com/JudgeOnline/logout.php";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
    }

    @Override
    public boolean checkBzojAccount(String username, String password) {
        bzojLogout();
        String url = "http://www.lydsy.com/JudgeOnline/login.php";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("user_id", username);
        map.add("password", password);
        map.add("submit", "Submit");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        log.info(response.getBody());
        return response.getBody().contains("-2");
    }

    @Override
    @Async
    @Transactional
    public void getAcProblemsByBzojAccount(OjAccount bzojAccount) {
        BaseHttpClient httpClient = new BaseHttpClient();
        String url = "http://www.lydsy.com/JudgeOnline/status.php?problem_id=&user_id=" + bzojAccount.getAccount() + "&language=-1&jresult=4";
        try {
            while(url != null) {
                Document document = Jsoup.connect(url).get();
                Elements table = document.body().selectFirst("table[align=center]").selectFirst("tbody").select("tr");
                for (int i = 1; i < table.size(); ++i) {
                    Elements line = table.get(i).select("td");
                    String proId = line.get(2).selectFirst("a").text();
                    Timestamp time = Timestamp.valueOf(line.get(8).text());
                    if (problemRepository.findByOjNameAndProblemId("HYSBZ", proId).isEmpty()) {
                        String title = Jsoup.connect("http://www.lydsy.com/JudgeOnline/problem.php?id=" + proId).get().selectFirst("h2").text().split(" ")[1];
                        problemService.addProblem("HYSBZ", proId, title);
                    }
                    Problem problem = problemService.findProblem("HYSBZ", proId);
                    if(problemService.addProblemAcRecord(problem, bzojAccount, time.getTime()+8*60*60*1000)) {
                        break;
                    }
                }
                Elements links = document.body().select("a");
                for (Element link : links) {
                    if (link.text().equals("Next Page")) {
                        String nextPage = "http://www.lydsy.com/JudgeOnline/" + link.attr("href");
                        if(url.equals(nextPage)) url = null;
                        else url = nextPage;
                    }
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Async
    public void getAllAcProblems() {
        List<OjAccount> ojAccounts = ojAccountRepository.findAllByOjName("BZOJ");
        for(OjAccount ojAccount : ojAccounts) {
            getAcProblemsByBzojAccount(ojAccount);
        }
    }
}
