package com.DB_PASSWORD_REDACTED.trip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.trip.Guguns;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.Sidos;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.TripContentDto;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.TripPost;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.TripRegionDto;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.contentData;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.contentTypes;
import com.DB_PASSWORD_REDACTED.trip.repository.PostRepository;
import com.DB_PASSWORD_REDACTED.trip.repository.TripRepository;
import com.DB_PASSWORD_REDACTED.trip.util.DBUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {

	private final TripRepository repo;

	private final PostRepository postrepo;

	
	public List<Sidos> ContentData() throws ClassNotFoundException, SQLException {
		List<Sidos> sidos = repo.getAllSidos();
		
        for (Sidos sido : sidos) {
            List<Guguns> guguns = repo.getGugunsBySidoCode(sido.getSidoCode());
            sido.setGuguns(guguns);
        }
        return sidos;
		
	}

	public List<contentTypes> getContentTypes(){
		return repo.getContentTypes();
	}
	public List<contentData> contentSearch(String sido, String gugun, String content) {
		
		return repo.contentSearch(sido, gugun, content);
	}
	
	public List<TripPost> selectPostList(int offset, int limit) throws Exception {
        return postrepo.getPagedPosts(offset, limit);
    }
	
	public int countPosts() throws Exception {
        return postrepo.getTotalPostCount();
    }
	
	public List<TripPost> searchPosts(String field, String keyword, int offset, int limit) throws SQLException, ClassNotFoundException {
	    List<TripPost> allPosts = postrepo.getAllPosts();  // 모든 게시글 가져오기
	    List<TripPost> filteredPosts = new ArrayList<>();

	    for (TripPost post : allPosts) {
	        String target = "";
	        if ("title".equals(field)) {
	            target = post.getTitle();
	        } else if ("author".equals(field)) {
	            target = post.getAuthor();
	        }

	        if (kmpSearch(target, keyword)) {
	            filteredPosts.add(post);
	        }
	    }

	    // 페이징 처리
	    int end = Math.min(offset + limit, filteredPosts.size());
	    if (offset > end) return new ArrayList<>();
	    return filteredPosts.subList(offset, end);
	}

	public int countSearchPosts(String field, String keyword) throws SQLException, ClassNotFoundException {
	    List<TripPost> allPosts = postrepo.getAllPosts();
	    int count = 0;

	    for (TripPost post : allPosts) {
	        String target = "";
	        if ("title".equals(field)) {
	            target = post.getTitle();
	        } else if ("author".equals(field)) {
	            target = post.getAuthor();
	        }

	        if (kmpSearch(target, keyword)) {
	            count++;
	        }
	    }

	    return count;
	}

	// KMP 알고리즘
	private boolean kmpSearch(String text, String pattern) {
	    if (text == null || pattern == null) return false;

	    int[] lps = computeLPSArray(pattern);
	    int i = 0, j = 0;

	    while (i < text.length()) {
	        if (pattern.charAt(j) == text.charAt(i)) {
	            i++;
	            j++;
	        }

	        if (j == pattern.length()) {
	            return true;
	        } else if (i < text.length() && pattern.charAt(j) != text.charAt(i)) {
	            if (j != 0) {
	                j = lps[j - 1];
	            } else {
	                i++;
	            }
	        }
	    }

	    return false;
	}

	private int[] computeLPSArray(String pattern) {
	    int[] lps = new int[pattern.length()];
	    int len = 0;
	    int i = 1;

	    while (i < pattern.length()) {
	        if (pattern.charAt(i) == pattern.charAt(len)) {
	            len++;
	            lps[i] = len;
	            i++;
	        } else {
	            if (len != 0) {
	                len = lps[len - 1];
	            } else {
	                lps[i] = 0;
	                i++;
	            }
	        }
	    }
	    return lps;
	}

	


}
