//  AudioTrack.swift
//  RmxAudioPlayer
//
//  Created by codinronan on 3/29/18.
//

import AVFoundation

final class AudioTrack: AVPlayerItem {
    var isStream = false
    var trackId: String?
    var assetUrl: URL?
    var albumArt: URL?
    var artist: String?
    var album: String?
    var title: String?
    var httpHeaders: [String: String]?

    class func initWithDictionary(_ trackInfo: [String : Any]?) -> AudioTrack? {
        guard
            let trackInfo = trackInfo,
            let trackId = trackInfo["trackId"] as? String,
            !trackId.isEmpty,
            let assetUrlString = trackInfo["assetUrl"] as? String,
            let assetUrl = URL(string: assetUrlString)
        else {
            return nil
        }
   
        let headers = [
    "Authorization": "Bearer YourBearerTokenHere",
    // Include any other headers your server requires
]
        let httpHeaders = trackInfo["httpHeaders"] as? [String: String]
        
        let options: [String: Any]? = httpHeaders != nil ? ["AVURLAssetHTTPHeaderFieldsKey": httpHeaders!] : nil
        let asset = AVURLAsset(url: assetUrl, options: options)
        let track = AudioTrack(asset: asset)
        track.canUseNetworkResourcesForLiveStreamingWhilePaused = true

        if let isStreamStr = trackInfo["isStream"] as? NSString {
            track.isStream = isStreamStr.boolValue
        }
        
        let albumArt = trackInfo["albumArt"] as? String
        track.albumArt = albumArt != nil ? URL(string: albumArt!) : nil
        
        track.trackId = trackId
        track.assetUrl = assetUrl
        track.artist = trackInfo["artist"] as? String
        track.album = trackInfo["album"] as? String
        track.title = trackInfo["title"] as? String
        
        return track
    }

    func toDict() -> [String : Any]? {
        [
            "isStream": NSNumber(value: isStream),
            "trackId": trackId ?? "",
            "assetUrl": assetUrl?.absoluteString ?? "",
            "albumArt": albumArt?.absoluteString ?? "",
            "artist": artist ?? "",
            "album": album ?? "",
            "title": title ?? "",
            "httpHeaders": httpHeaders ?? ""
        ]
    }
}
