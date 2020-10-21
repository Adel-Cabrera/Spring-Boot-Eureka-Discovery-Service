package com.darkonnen.photoapp.api.users.data;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.darkonnen.photoapp.api.users.ui.models.AlbumResponseModel;

@FeignClient(name="albums-ws")
public interface AlbumServiceClient {
	
	@GetMapping(path="/users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable("id") String id);
}
