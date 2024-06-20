import json
import time

import requests
import pymysql

# 建立数据库连接
connection = pymysql.connect(
    host='127.0.0.1',  # 数据库主机名
    port=3306,  # 数据库端口号，默认为3306
    user='root',  # 数据库用户名
    passwd='162881772',  # 数据库密码
    db='api_final_work',  # 数据库名称
    charset='utf8'  # 字符编码
)

# 创建游标对象
cursor = connection.cursor()

# 央视网快看
url = "https://www.douyin.com/aweme/v1/web/aweme/post/?device_platform=webapp&aid=6383&channel=channel_pc_web&sec_user_id=MS4wLjABAAAAC7r8UcpQchuQDP6MDlXoBVK3vrUHPDOy07dtII5uKeI&max_cursor=0&locate_query=false&show_live_replay_strategy=1&need_time_list=1&time_list_query=0&whale_cut_token=&cut_version=1&count=18&publish_video_strategy_type=2&update_version_code=170400&pc_client_type=1&version_code=290100&version_name=29.1.0&cookie_enabled=true&screen_width=1536&screen_height=864&browser_language=zh-CN&browser_platform=Win32&browser_name=Chrome&browser_version=126.0.0.0&browser_online=true&engine_name=Blink&engine_version=126.0.0.0&os_name=Windows&os_version=10&cpu_core_num=16&device_memory=8&platform=PC&downlink=10&effective_type=4g&round_trip_time=150&webid=7376296809385215523&verifyFp=verify_lwz49qbp_3145b2fa_b969_0796_e405_2559402499fc&fp=verify_lwz49qbp_3145b2fa_b969_0796_e405_2559402499fc&msToken=37fXHClewQWwBNtjYzLodqwANWNgLaLbcsHvAtjVoAckwnsdD6x_wnYrF8cQyy1r_rF96rLNF7dmPehE1jcl3NBv6X_L7uYmlyZp73OzaCSgGajjoI-0Akmf6Bckk4Q%3D&a_bogus=Q7R0B5hXDE2iXfW65I%2FLfY3q6A33YD4a0trEMD2fHV3Gzy39HMPN9exoWh0vl9mjLG%2FlIb6jy4hCYpBMicQnA3v6HSRKl2Ck-g00t-P2so0j5ZhjCfuDrURF-vzWt-Bd-Jd3xcXmy7dbF8RplnAJ5k1cthMea5g%3D"
# 敦煌老马
# url = "https://www.douyin.com/aweme/v1/web/aweme/post/?device_platform=webapp&aid=6383&channel=channel_pc_web&sec_user_id=MS4wLjABAAAAO2ZAL5jSKmKArB9YwHFhLVS1NoNXT-GaeBh69m2hNGI&max_cursor=0&locate_query=false&show_live_replay_strategy=1&need_time_list=1&time_list_query=0&whale_cut_token=&cut_version=1&count=18&publish_video_strategy_type=2&update_version_code=170400&pc_client_type=1&version_code=290100&version_name=29.1.0&cookie_enabled=true&screen_width=1536&screen_height=864&browser_language=zh-CN&browser_platform=Win32&browser_name=Chrome&browser_version=126.0.0.0&browser_online=true&engine_name=Blink&engine_version=126.0.0.0&os_name=Windows&os_version=10&cpu_core_num=16&device_memory=8&platform=PC&downlink=10&effective_type=4g&round_trip_time=200&webid=7376296809385215523&msToken=_DvHWhFCvZ2cBd2Fq2JYZE5kdP731ho3zAY7hcthy6exEE0HId1ok0O3iK9oiLmRZHmSRseNhlrL8NnUxNrXJtgAlPsPxRF19D8Zo_BiRgkZh88YJPmq&a_bogus=E7m0BRukDifN6fSg5InLfY3q6Xe3Ys090trEME2f4xVs4639HMOF9exLl7GvPVyjLG%2FlIb6jy4hCYpBMicQnA3v6HSRKl2Ck-g00t-P2so0j5ZhjCfuDrURF-vzWt-Bd-Jd3xcXmy7dbF8RplnAJ5k1cthMea6W%3D&verifyFp=verify_lwz49qbp_3145b2fa_b969_0796_e405_2559402499fc&fp=verify_lwz49qbp_3145b2fa_b969_0796_e405_2559402499fc"
# 新华网
# url = "https://www.douyin.com/aweme/v1/web/aweme/post/?device_platform=webapp&aid=6383&channel=channel_pc_web&sec_user_id=MS4wLjABAAAAukfxyGQmo3HK9N26B8v6SkhCwbtbjEqlThz1U_zxkcI&max_cursor=1717538400000&locate_query=false&show_live_replay_strategy=1&need_time_list=0&time_list_query=0&whale_cut_token=&cut_version=1&count=18&publish_video_strategy_type=2&update_version_code=170400&pc_client_type=1&version_code=290100&version_name=29.1.0&cookie_enabled=true&screen_width=1536&screen_height=864&browser_language=zh-CN&browser_platform=Win32&browser_name=Chrome&browser_version=126.0.0.0&browser_online=true&engine_name=Blink&engine_version=126.0.0.0&os_name=Windows&os_version=10&cpu_core_num=16&device_memory=8&platform=PC&downlink=10&effective_type=4g&round_trip_time=200&webid=7376296809385215523&msToken=LU0SiCuVkVTNrIP5gZ8hAVKJJ3PRvMyWKOkv-NXpY3_swOlPtX5a73UXTQ_rEvYdct6kyQxjcXqHj2Ju2qRYku2zMP35lF07EphW2J_GPu4v0Fs1NM3jsLSx2EEi-lg%3D&a_bogus=EyW0Mf8hdkDP6fWD5InLfY3q6Uq3Ys020trEME2fwnVsby39HMOF9exLl9GvQnbjLG%2FlIbfjy4hCTpNMicQnA3v6HSRKl2Ck-g00t-P2so0j5ZhjCfuDrURF-vzWt-Bd-Jd3EcvMoJKSFRi0AIee-wHvnwVxaptd&verifyFp=verify_lwz49qbp_3145b2fa_b969_0796_e405_2559402499fc&fp=verify_lwz49qbp_3145b2fa_b969_0796_e405_2559402499fc"
payload = {}
headers = {
    'accept': 'application/json, text/plain, */*',
    'accept-language': 'zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6',
    'cookie': 'ttwid=1%7C2sXMeZ8YJ7xhRtLgSG80oGKtaVpXhwUPvFIXWBxlGKw%7C1717427957%7C00df55a134ae61a93a226011f13970d607fa70f7ed945d5ee00fb5ce4c397a42; dy_swidth=1536; dy_sheight=864; FORCE_LOGIN=%7B%22videoConsumedRemainSeconds%22%3A180%7D; xgplayer_user_id=107640886761; passport_csrf_token=08c6e42a983c464d690910b6060b2814; passport_csrf_token_default=08c6e42a983c464d690910b6060b2814; bd_ticket_guard_client_web_domain=2; s_v_web_id=verify_lwz49qbp_3145b2fa_b969_0796_e405_2559402499fc; d_ticket=b72b8525db485bc3b801ce430002a0fa58b6b; passport_assist_user=CkBqPnP4fjo1DHVb-88bhAym_cYWWxlinilMPQHQo1iCl1ga3wY6M82vtNKeG83O1uWn6Oouq-lRthXaJ-mag9XAGkoKPB9iG-WUDAGl-mE8wGoLRIm8fVM6bhzfUe2MJtHE6NiclnCr740oYCykfUH92pVp9fPMV1e2cAQ2FrttHhCJh9MNGImv1lQgASIBA8rieYU%3D; n_mh=jszYg9i6gHfZTX7KEHEStvXb1SevpLati_lZm13_1Rk; sso_auth_status=e555a09eb29ca437f30010d4fbc3ddfb; sso_auth_status_ss=e555a09eb29ca437f30010d4fbc3ddfb; sso_uid_tt=20009eb24843163a723567c22521603a; sso_uid_tt_ss=20009eb24843163a723567c22521603a; toutiao_sso_user=0026ae69ec6f3188011b34955f619528; toutiao_sso_user_ss=0026ae69ec6f3188011b34955f619528; sid_ucp_sso_v1=1.0.0-KGY1NzVlM2M5NTg5NWYyNjJlMGQxMzQ5MGRiMzc5OWExZWQ4Njc3NDMKHgiryqC6h41LEJW-97IGGO8xIAww0emdnAY4AkDxBxoCaGwiIDAwMjZhZTY5ZWM2ZjMxODgwMTFiMzQ5NTVmNjE5NTI4; ssid_ucp_sso_v1=1.0.0-KGY1NzVlM2M5NTg5NWYyNjJlMGQxMzQ5MGRiMzc5OWExZWQ4Njc3NDMKHgiryqC6h41LEJW-97IGGO8xIAww0emdnAY4AkDxBxoCaGwiIDAwMjZhZTY5ZWM2ZjMxODgwMTFiMzQ5NTVmNjE5NTI4; passport_auth_status=6150456e44f5e9461a56447d1b5e454e%2C373600f01ac79f3ab1b83076c3b18e23; passport_auth_status_ss=6150456e44f5e9461a56447d1b5e454e%2C373600f01ac79f3ab1b83076c3b18e23; uid_tt=59533be2b511c027f2b4f3573b81691d; uid_tt_ss=59533be2b511c027f2b4f3573b81691d; sid_tt=f7aadeaf36a144431c1b52a864be659e; sessionid=f7aadeaf36a144431c1b52a864be659e; sessionid_ss=f7aadeaf36a144431c1b52a864be659e; publish_badge_show_info=%220%2C0%2C0%2C1717428007704%22; _bd_ticket_crypt_doamin=2; _bd_ticket_crypt_cookie=529677c5967f64d028e54eaef8513b06; __security_server_data_status=1; sid_guard=f7aadeaf36a144431c1b52a864be659e%7C1717428008%7C5183984%7CFri%2C+02-Aug-2024+15%3A19%3A52+GMT; sid_ucp_v1=1.0.0-KDcxZGM4MGUzYTVmNWIyYjdjZmMxY2I0Nzk5YmNhZGM1OTg0OWY2OWIKGgiryqC6h41LEKi-97IGGO8xIAw4AkDxB0gEGgJscSIgZjdhYWRlYWYzNmExNDQ0MzFjMWI1MmE4NjRiZTY1OWU; ssid_ucp_v1=1.0.0-KDcxZGM4MGUzYTVmNWIyYjdjZmMxY2I0Nzk5YmNhZGM1OTg0OWY2OWIKGgiryqC6h41LEKi-97IGGO8xIAw4AkDxB0gEGgJscSIgZjdhYWRlYWYzNmExNDQ0MzFjMWI1MmE4NjRiZTY1OWU; xgplayer_device_id=27379808062; download_guide=%223%2F20240603%2F0%22; pwa2=%220%7C0%7C3%7C0%22; store-region=cn-bj; store-region-src=uid; SEARCH_RESULT_LIST_TYPE=%22single%22; WallpaperGuide=%7B%22showTime%22%3A1717429456717%2C%22closeTime%22%3A0%2C%22showCount%22%3A1%2C%22cursor1%22%3A69%2C%22cursor2%22%3A0%2C%22hoverTime%22%3A1717465446788%7D; volume_info=%7B%22isUserMute%22%3Afalse%2C%22isMute%22%3Atrue%2C%22volume%22%3A0.5%7D; douyin.com; device_web_cpu_core=16; device_web_memory_size=8; architecture=amd64; home_can_add_dy_2_desktop=%220%22; strategyABtestKey=%221717568002.969%22; csrf_session_id=0c97eab7f8e6d4dd739a338b79589eee; xg_device_score=7.417294117647058; stream_player_status_params=%22%7B%5C%22is_auto_play%5C%22%3A0%2C%5C%22is_full_screen%5C%22%3A0%2C%5C%22is_full_webscreen%5C%22%3A0%2C%5C%22is_mute%5C%22%3A1%2C%5C%22is_speed%5C%22%3A1%2C%5C%22is_visible%5C%22%3A0%7D%22; __ac_nonce=06660020500993074eb89; __ac_signature=_02B4Z6wo00f01TQB3NAAAIDB63a6y1SHo2E0IdhAACtjff; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWl0ZXJhdGlvbi12ZXJzaW9uIjoxLCJiZC10aWNrZXQtZ3VhcmQtcmVlLXB1YmxpYy1rZXkiOiJCR2NGMGpyd3UxVlN0ZFcyNTdXVjYzNFByMHREL2tuc3pmK0NuSVRXNnFNbTFmYjJqZXQ4OXU0aHZldEdDK3pnSzNmRVY1WHQzRnFaSjBNanNRRndDem89IiwiYmQtdGlja2V0LWd1YXJkLXdlYi12ZXJzaW9uIjoxfQ%3D%3D; passport_fe_beating_status=false; msToken=xvv1OSDEHHNvRcnG0HXxY2WLVlTmKpzhNC8SRBpy5VIMbCTJJCuOe5ZJ24K20OckMd3aNG8LD0Ov17mT4iMw_6oGBcAW7ZYn-qKyFJ5a5epg31Ajw74tnsPovjARj_o=; odin_tt=99d18495d67667d4466cdc4c393aa1fd17ce4b98e6fd93bd7ad02bef271e2b9bc7547ff48dfd3070fba6df296f4ceb5da3d0968160c46e4b7eb82181adab7d9a; IsDouyinActive=true; stream_recommend_feed_params=%22%7B%5C%22cookie_enabled%5C%22%3Atrue%2C%5C%22screen_width%5C%22%3A1536%2C%5C%22screen_height%5C%22%3A864%2C%5C%22browser_online%5C%22%3Atrue%2C%5C%22cpu_core_num%5C%22%3A16%2C%5C%22device_memory%5C%22%3A8%2C%5C%22downlink%5C%22%3A10%2C%5C%22effective_type%5C%22%3A%5C%224g%5C%22%2C%5C%22round_trip_time%5C%22%3A150%7D%22',
    'priority': 'u=1, i',
    'referer': 'https://www.douyin.com/user/MS4wLjABAAAAC7r8UcpQchuQDP6MDlXoBVK3vrUHPDOy07dtII5uKeI',
    'sec-ch-ua': '" Not A;Brand";v="99", "Chromium";v="126", "Google Chrome";v="126"',
    'sec-ch-ua-mobile': '?0',
    'sec-ch-ua-platform': '"Windows"',
    'sec-fetch-dest': 'empty',
    'sec-fetch-mode': 'cors',
    'sec-fetch-site': 'same-origin',
    'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36'
}

response = requests.request("GET", url, headers=headers, data=payload)
response.encoding = 'utf-8'
result_list = json.loads(response.text)
# print(response.text)

# 获得视频列表
video_list = result_list['aweme_list']

for video_data in video_list:
    # 获得视频url
    video = video_data['video']['play_addr']['url_list'][0]
    # 获得视频名称
    title = video_data['desc']
    # 视频id
    video_id = video_data['aweme_id']
    # 作者id
    author_id = video_data['author']['uid']
    # 作者名称
    author_name = video_data['author']['nickname']
    # 点赞数
    digg_count = video_data['statistics']['digg_count']
    # 当前时间
    currentTime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())

    print(video, title, video_id, author_id, author_name, digg_count)

    # # 查询是否有用户
    # sql = "SELECT * FROM `user` WHERE id={}".format(author_id)
    # cursor.execute(sql)
    # results = cursor.fetchone()
    # # 如果没有找到用户
    # if results is None:
    #     # 插入用户
    #     sql = "INSERT INTO `user`(`id`, username)"
    # 下载视频
    video_res = requests.get(video, headers=headers)
    video_url = "C:/Users/dell/Videos/APIVideo/{}.mp4".format(video_id)
    with open(video_url, 'wb') as f:
        f.write(video_res.content)

    sql = "INSERT INTO `video`(`user_id`, `video_id`, `name`, `url`, `digg_count`) VALUES ('{0}', '{1}', '{2}', '{3}', {4})".format(
        author_id, video_id, title, video_url, int(digg_count))

    print(sql)
    cursor.execute(sql)
    connection.commit()
