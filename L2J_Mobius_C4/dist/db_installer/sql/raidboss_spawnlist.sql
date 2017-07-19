-- ----------------------------
-- Table structure for raidboss_spawnlist
-- ----------------------------

DROP TABLE IF EXISTS `raidboss_spawnlist`;
CREATE TABLE `raidboss_spawnlist` (
  `boss_id` int(11) NOT NULL DEFAULT 0,
  `amount` int(11) NOT NULL DEFAULT 0,
  `loc_x` int(11) NOT NULL DEFAULT 0,
  `loc_y` int(11) NOT NULL DEFAULT 0,
  `loc_z` int(11) NOT NULL DEFAULT 0,
  `heading` int(11) NOT NULL DEFAULT 0,
  `respawn_min_delay` int(11) NOT NULL DEFAULT 43200,
  `respawn_max_delay` int(11) NOT NULL DEFAULT 129600,
  `respawn_time` bigint(20) NOT NULL DEFAULT 0,
  `currentHp` decimal(8,0) DEFAULT NULL,
  `currentMp` decimal(8,0) DEFAULT NULL,
  PRIMARY KEY (`boss_id`,`loc_x`,`loc_y`,`loc_z`)
);

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `raidboss_spawnlist` VALUES
(10001, 1, -54464, 146572, -2400, 0, 43200, 129600, 0, 23712, 514),
(10004, 1, -94101, 100238, -3012, 0, 43200, 129600, 0, 66433, 732),
(10007, 1, 124240, 75376, -2800, 0, 43200, 129600, 0, 100965, 1178),
(10010, 1, 113920, 52960, -3735, 0, 43200, 129600, 0, 153521, 1976),
(10013, 1, 169744, 11920, -2732, 0, 43200, 129600, 0, 132487, 1660),
(10016, 1, 76787, 245775, -10376, 0, 43200, 129600, 0, 94188, 2302),
(10019, 1, 7352, 169433, -3172, 0, 43200, 129600, 0, 52415, 576),
(10020, 1, 90365, 125716, -1632, 0, 43200, 129600, 0, 39603, 860),
(10023, 1, 27181, 101830, -3192, 0, 43200, 129600, 0, 73549, 860),
(10026, 1, 92976, 7920, -3914, 0, 43200, 129600, 0, 69124, 1598),
(10029, 1, 54941, 206705, -3728, 0, 43200, 129600, 0, 156190, 1848),
(10032, 1, 88532, 245798, -10376, 25874, 43200, 129600, 0, 186648, 2640),
(10035, 1, 180968, 12035, -2720, 0, 43200, 129600, 0, 241630, 2988),
(10038, 1, -57366, 186276, -4804, 0, 43200, 129600, 0, 30283, 668),
(10041, 1, 10525, 126890, -3132, 0, 43200, 129600, 0, 41308, 894),
(10044, 1, 107792, 27728, -3488, 0, 43200, 129600, 0, 64765, 1476),
(10047, 1, 116352, 27648, -3319, 0, 43200, 129600, 0, 69124, 1598),
(10050, 1, 125520, 27216, -3632, 0, 43200, 129600, 0, 126124, 1722),
(10051, 1, 117760, -9072, -3264, 0, 43200, 129600, 0, 186648, 2640),
(10054, 1, 113432, 16403, 3960, 0, 43200, 129600, 0, 250509, 3348),
(10057, 1, 107056, 168176, -3456, 0, 43200, 129600, 0, 60514, 1356),
(10060, 1, -60428, 188264, -4512, 0, 43200, 129600, 0, 21374, 546),
(10063, 1, -91009, 116339, -2908, 0, 43200, 129600, 0, 82617, 894),
(10064, 1, 92528, 84752, -3703, 0, 43200, 129600, 0, 50445, 1062),
(10067, 1, 94992, -23168, -2176, 0, 43200, 129600, 0, 140823, 1784),
(10070, 1, 125600, 50100, -3600, 0, 43200, 129600, 0, 82665, 1976),
(10073, 1, 143265, 110044, -3944, 0, 43200, 129600, 0, 239476, 2918),
(10076, 1, -61041, 127347, -2512, 0, 43200, 129600, 0, 26207, 576),
(10079, 1, 53794, 102660, -529, 0, 43200, 129600, 0, 61688, 732),
(10082, 1, 88554, 140646, -2960, 0, 43200, 129600, 0, 48537, 1028),
(10085, 1, 66944, 67504, -3704, 0, 43200, 129600, 0, 108514, 1296),
(10088, 1, 90848, 16368, -5296, 0, 43200, 129600, 0, 211294, 1238),
(10089, 1, 165424, 93776, -2992, 0, 43200, 129600, 0, 91885, 2236),
(10092, 1, 116151, 16227, 1944, 0, 43200, 129600, 0, 196324, 2988),
(10095, 1, -37799, 198120, -2200, 0, 43200, 129600, 0, 31723, 700),
(10098, 1, 123570, 133506, -3156, 0, 43200, 129600, 0, 82617, 894),
(10099, 1, 64048, 16048, -3536, 0, 43200, 129600, 0, 58430, 1296),
(10102, 1, 113840, 84256, -2480, 0, 43200, 129600, 0, 121028, 1356),
(10103, 1, 135872, 94592, -3735, 0, 43200, 129600, 0, 82665, 1976),
(10106, 1, 173880, -11412, -2880, 0, 43200, 129600, 0, 98098, 2502),
(10109, 1, 152660, 110387, -5520, 0, 43200, 129600, 0, 248996, 3274),
(10112, 1, 116219, 139458, -3124, 0, 43200, 129600, 0, 33216, 732),
(10115, 1, 94000, 197500, -3300, 0, 43200, 129600, 0, 93684, 1062),
(10118, 1, 50883, 146764, -3077, 0, 43200, 129600, 0, 82617, 894),
(10119, 1, 121872, 64032, -3536, 0, 43200, 129600, 0, 163059, 1660),
(10122, 1, 86300, -8200, -3000, 0, 43200, 129600, 0, 84968, 2040),
(10125, 1, 170656, 85184, -2000, 0, 43200, 129600, 0, 318636, 2640),
(10126, 1, 116263, 15916, 6992, 0, 43200, 129600, 0, 386945, 3644),
(10127, 1, -47634, 219274, -1936, 0, 43200, 129600, 0, 49872, 546),
(10128, 1, 17671, 179134, -3016, 0, 43200, 129600, 0, 37942, 828),
(10131, 1, 75488, -9360, -2720, 0, 43200, 129600, 0, 71339, 1660),
(10134, 1, 87536, 75872, -3591, 0, 43200, 129600, 0, 50445, 1062),
(10137, 1, 125280, 102576, -3305, 0, 43200, 129600, 0, 82665, 1976),
(10140, 1, 191975, 56959, -7616, 0, 43200, 129600, 0, 186648, 2640),
(10143, 1, 113102, 16002, 6992, 0, 43200, 129600, 0, 255158, 3644),
(10146, 1, -13698, 213796, -3300, 0, 43200, 129600, 0, 18330, 456),
(10149, 1, -12652, 138200, -3120, 0, 43200, 129600, 0, 26207, 576),
(10152, 1, 43787, 124067, -2512, 0, 43200, 129600, 0, 41308, 894),
(10155, 1, 73520, 66912, -3728, 0, 43200, 129600, 0, 100890, 1062),
(10158, 1, 77104, 5408, -3088, 0, 43200, 129600, 0, 235690, 1538),
(10159, 1, 124984, 43200, -3625, 0, 43200, 129600, 0, 80375, 1912),
(10162, 1, 194107, 53884, -4368, 0, 43200, 129600, 0, 294240, 2302),
(10163, 1, 130500, 59098, 3584, 0, 43200, 129600, 0, 241630, 2988),
(10166, 1, -21778, 152065, -2636, 0, 43200, 129600, 0, 48671, 576),
(10169, 1, -54517, 170321, -2700, 0, 43200, 129600, 0, 123377, 732),
(10170, 1, 26108, 122256, -3488, 0, 43200, 129600, 0, 46672, 994),
(10173, 1, 75968, 110784, -2512, 0, 43200, 129600, 0, 60514, 1356),
(10176, 1, 92544, 115232, -3200, 0, 43200, 129600, 0, 82665, 1976),
(10179, 1, 181814, 52379, -4344, 0, 43200, 129600, 0, 94188, 2302),
(10182, 1, 41966, 215417, -3728, 0, 43200, 129600, 0, 100502, 2707),
(10185, 1, 88143, 166365, -3388, 0, 43200, 129600, 0, 41308, 894),
(10188, 1, 88102, 176262, -3012, 0, 43200, 129600, 0, 66433, 732),
(10189, 1, 68677, 203149, -3192, 0, 43200, 129600, 0, 39603, 860),
(10192, 1, 125920, 190208, -3291, 0, 43200, 129600, 0, 56386, 1238),
(10198, 1, 102656, 157424, -3735, 0, 43200, 129600, 0, 343032, 2988),
(10199, 1, 108096, 157408, -3688, 0, 43200, 129600, 0, 214875, 3130),
(10202, 1, 119760, 157392, -3744, 0, 43200, 129600, 0, 248996, 3274),
(10205, 1, 123808, 153408, -3671, 0, 43200, 129600, 0, 220403, 3420),
(10208, 1, 73776, 201552, -3760, 0, 43200, 129600, 0, 50445, 1062),
(10211, 1, 76461, 193228, -3208, 0, 43200, 129600, 0, 43053, 928),
(10214, 1, 112112, 209936, -3616, 0, 43200, 129600, 0, 50445, 1062),
(10217, 1, 89904, 105712, -3292, 0, 43200, 129600, 0, 71339, 1660),
(10220, 1, 113551, 17083, -2120, 0, 43200, 129600, 0, 247349, 3202),
(10223, 1, 43062, 152492, -2294, 0, 43200, 129600, 0, 41308, 894),
(10226, 1, 104240, -3664, -3392, 0, 43200, 129600, 0, 193664, 2436),
(10229, 1, 137568, -19488, -3552, 0, 43200, 129600, 0, 367428, 3348),
(10230, 1, 66672, 46704, -3920, 0, 43200, 129600, 0, 87272, 2104),
(10233, 1, 185800, -26500, -2000, 0, 43200, 129600, 0, 209541, 2918),
(10234, 1, 120080, 111248, -3047, 0, 43200, 129600, 0, 201006, 2640),
(10235, 1, 116400, -62528, -3264, 0, 43200, 129600, 0, 245570, 3130),
(10238, 1, 159841, 38370, -3648, 0, 43200, 129600, 0, 102733, 2778),
(10241, 1, 165984, 88048, -2384, 0, 43200, 129600, 0, 153521, 1976),
(10244, 1, 187360, 45840, -5856, 0, 43200, 129600, 0, 367428, 3348),
(10245, 1, 172000, 55000, -5400, 0, 43200, 129600, 0, 206546, 3568),
(10248, 1, 127903, -13399, -3720, 0, 43200, 129600, 0, 352790, 3130),
(10249, 1, 147104, -20560, -3377, 0, 43200, 129600, 0, 219192, 3348),
(10252, 1, 192376, 22087, -3608, 0, 43200, 129600, 0, 211425, 2988),
(10255, 1, 170048, -24896, -3440, 0, 43200, 129600, 0, 318636, 2640),
(10256, 1, 170320, 42640, -4832, 0, 43200, 129600, 0, 94188, 2302),
(10259, 1, 42050, 208107, -3752, 0, 43200, 129600, 0, 269844, 1976),
(10260, 1, 93120, 19440, -3607, 0, 43200, 129600, 0, 112383, 1356),
(10263, 1, 144400, -28192, -1920, 0, 43200, 129600, 0, 190791, 2778),
(10266, 1, 188983, 13647, -2672, 0, 43200, 129600, 0, 250509, 3348),
(10269, 1, 123504, -23696, -3481, 0, 43200, 129600, 0, 211425, 2988),
(10272, 1, 49194, 127999, -3161, 0, 43200, 129600, 0, 60567, 668),
(10276, 1, 154088, -14116, -3736, 0, 43200, 129600, 0, 367428, 3348),
(10277, 1, 54651, 180269, -4976, 0, 43200, 129600, 0, 142678, 1660),
(10280, 1, 85622, 88766, -5120, 0, 43200, 129600, 0, 269844, 1976),
(10281, 1, 151053, 88124, -5424, 0, 43200, 129600, 0, 343032, 2988),
(10282, 1, 179311, -7632, -4896, 0, 43200, 129600, 0, 367428, 3348),
(10283, 1, 184410, -10111, -5488, 0, 43200, 129600, 0, 255960, 3793),
(10286, 1, 185000, -13000, -5488, 0, 43200, 129600, 0, 255960, 3793),
(10293, 1, 134672, -115600, -1216, 0, 43200, 129600, 0, 206546, 3568),
(10299, 1, 148160, -73808, -4919, 0, 43200, 129600, 0, 111981, 3718),
(10302, 1, 145504, -81664, -6016, 0, 43200, 129600, 0, 112730, 4022),
(10305, 1, 145008, -84992, -6240, 0, 43200, 129600, 0, 225198, 4256),
(10309, 1, 115552, -39200, -2480, 0, 43200, 129600, 0, 111981, 3718),
(10312, 1, 109216, -36160, -938, 0, 43200, 129600, 0, 112730, 4022),
(10315, 1, 105584, -43024, -1728, 0, 43200, 129600, 0, 225198, 4256),
(10319, 1, 184542, -106330, -6304, 0, 43200, 129600, 0, 257725, 4100),
(10322, 1, 93296, -75104, -1824, 0, 43200, 129600, 0, 232323, 2708),
(10325, 1, 91008, -85904, -2736, 0, 43200, 129600, 0, 211425, 3718),
(10328, 1, 59331, -42403, -3003, 0, 7200, 10800, 0, 243663, 3718),
(10352, 1, -16843, 174890, -2984, 0, 43200, 129600, 0, 28471, 732),
(10354, 1, -16089, 184295, -3364, 0, 43200, 129600, 0, 41308, 894),
(10357, 1, -3451, 112819, -2995, 0, 43200, 129600, 0, 18330, 456),
(10360, 1, 29064, 179362, -3128, 0, 43200, 129600, 0, 23592, 606),
(10362, 1, -55791, 186903, -2856, 0, 43200, 129600, 0, 20324, 514),
(10365, 1, -62171, 190489, -3160, 0, 43200, 129600, 0, 47184, 606),
(10366, 1, -62342, 179572, -3088, 0, 43200, 129600, 0, 20324, 514),
(10369, 1, -45713, 111186, -3280, 0, 43200, 129600, 0, 22463, 576),
(10372, 1, 48000, 243376, -6611, 0, 43200, 129600, 0, 34766, 426),
(10373, 1, 9661, 76976, -3652, 0, 43200, 129600, 0, 18330, 456),
(10375, 1, 22523, 80431, -3188, 0, 43200, 129600, 0, 17383, 426),
(10378, 1, -53970, 84334, -3048, 0, 43200, 129600, 0, 17383, 426),
(10380, 1, -47412, 51647, -5659, 0, 43200, 129600, 0, 18330, 456),
(10383, 1, 51405, 153984, -3008, 0, 43200, 129600, 0, 33945, 860),
(10385, 1, 53418, 143534, -3332, 0, 43200, 129600, 0, 36903, 928),
(10388, 1, 40074, 102019, -790, 0, 43200, 129600, 0, 35407, 894),
(10391, 1, 45620, 120710, -2158, 0, 43200, 129600, 0, 65044, 828),
(10392, 1, 29891, 107201, -3572, 0, 43200, 129600, 0, 31134, 796),
(10394, 1, 101806, 200394, -3180, 0, 43200, 129600, 0, 80010, 994),
(10395, 1, 15000, 119000, -11900, 0, 43200, 129600, 0, 51869, 1356),
(10398, 1, 5000, 189000, -3728, 0, 43200, 129600, 0, 35407, 894),
(10401, 1, 117812, 102948, -3140, 0, 43200, 129600, 0, 31134, 796),
(10404, 1, 36048, 191352, -2524, 0, 43200, 129600, 0, 32522, 828),
(10407, 1, 115072, 112272, -3018, 0, 43200, 129600, 0, 80732, 2302),
(10410, 1, 72192, 125424, -3657, 0, 43200, 129600, 0, 50445, 1062),
(10412, 1, 81920, 113136, -3056, 0, 43200, 129600, 0, 55512, 1476),
(10415, 1, 128352, 138464, -3467, 0, 43200, 129600, 0, 35407, 894),
(10418, 1, 62416, 8096, -3376, 0, 43200, 129600, 0, 50083, 1296),
(10420, 1, 42032, 24128, -4704, 0, 43200, 129600, 0, 66930, 1538),
(10423, 1, 113600, 47120, -4640, 0, 43200, 129600, 0, 84968, 2040),
(10426, 1, -18053, -101274, -1580, 0, 43200, 129600, 0, 22463, 576),
(10429, 1, 172122, -214776, -3064, 0, 43200, 129600, 0, 22463, 576),
(10431, 1, 79648, 18320, -5232, 0, 43200, 129600, 0, 50083, 1296),
(10434, 1, 104096, -16896, -1803, 0, 43200, 129600, 0, 70856, 1976),
(10437, 1, 67296, 64128, -3723, 0, 43200, 129600, 0, 103738, 1356),
(10438, 1, 107000, 92000, -2272, 0, 43200, 129600, 0, 50083, 1296),
(10441, 1, 111440, 82912, -2912, 0, 43200, 129600, 0, 51869, 1356),
(10444, 1, 113232, 17456, -4384, 0, 43200, 129600, 0, 86145, 2640),
(10447, 1, 113200, 17552, -1424, 0, 43200, 129600, 0, 91375, 3058),
(10450, 1, 113600, 15104, 9559, 0, 43200, 129600, 0, 254211, 3568),
(10453, 1, 156704, -6096, -4185, 0, 43200, 129600, 0, 196324, 2988),
(10456, 1, 133632, 87072, -3623, 0, 43200, 129600, 0, 59249, 1598),
(10460, 1, 150304, 67776, -3688, 0, 43200, 129600, 0, 63062, 1722),
(10463, 1, 166288, 68096, -3264, 0, 43200, 129600, 0, 72830, 2040),
(10467, 1, 186192, 61472, -4160, 0, 43200, 129600, 0, 85133, 2570),
(10470, 1, 186896, 56276, -4576, 0, 43200, 129600, 0, 87120, 2708),
(10473, 1, 175712, 29856, -3776, 0, 43200, 129600, 0, 64995, 1784),
(10475, 1, 183568, 24560, -3184, 0, 43200, 129600, 0, 70856, 1976),
(10478, 1, 168288, 28368, -3632, 0, 43200, 129600, 0, 86145, 2640),
(10481, 1, 53517, 205413, -3728, 0, 43200, 129600, 0, 66938, 3718),
(10484, 1, 43160, 220463, -3680, 0, 43200, 129600, 0, 61148, 1660),
(10487, 1, 83056, 183232, -3616, 0, 43200, 129600, 0, 43238, 1062),
(10490, 1, 86528, 216864, -3584, 0, 43200, 129600, 0, 43238, 1062),
(10493, 1, 83174, 254428, -10873, 0, 43200, 129600, 0, 70856, 1976),
(10496, 1, 88300, 258000, -10200, 0, 43200, 129600, 0, 64995, 1784),
(10498, 1, 126624, 174448, -3056, 0, 43200, 129600, 0, 51869, 1356);