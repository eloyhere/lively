import type { Question } from './tcm'

export const questions: Question[] = [
  // ==================== 中医基础理论 ====================
  {
    id: 1,
    type: 'single',
    subject: 'basic',
    question: '下列哪项不是"证"的概念？',
    options: ['风寒感冒', '肝阳上亢', '心血亏虚', '恶寒发热'],
    answer: [3],
    explanation: '证是机体在疾病发展过程中某一阶段的病理概括，包括病变的部位、原因、性质以及邪正关系。恶寒发热是症状，不是证。',
    keyPoint: '证与症状的区别',
    difficulty: 1
  },
  {
    id: 2,
    type: 'single',
    subject: 'basic',
    question: '被称为"后天之本"的脏腑是？',
    options: ['心', '肝', '脾', '肾'],
    answer: [2],
    explanation: '脾主运化，为气血生化之源，人体出生后，生命活动的维持和气血津液的化生，都依赖于脾所运化的水谷精微，故称脾为"后天之本"。',
    keyPoint: '脏腑的生理功能',
    difficulty: 1
  },
  {
    id: 3,
    type: 'multiple',
    subject: 'basic',
    question: '下列属于五行相生关系的有？（多选）',
    options: ['木生火', '火生土', '土生金', '金生水', '水生木'],
    answer: [0, 1, 2, 3, 4],
    explanation: '五行相生的次序是：木生火，火生土，土生金，金生水，水生木。',
    keyPoint: '五行相生规律',
    difficulty: 1
  },
  {
    id: 4,
    type: 'judge',
    subject: 'basic',
    question: '中医认为"心主神明"，这里的"神明"仅指人的精神意识思维活动。',
    options: ['正确', '错误'],
    answer: [1],
    explanation: '心主神明不仅包括人的精神意识思维活动，还包括人的生命活动的外在表现，如面色、眼神、言语、应答、肢体活动等。',
    keyPoint: '心的生理功能',
    difficulty: 2
  },
  {
    id: 5,
    type: 'single',
    subject: 'basic',
    question: '下列哪项不属于六淫致病的共同特点？',
    options: ['外感性', '季节性', '传染性', '地域性'],
    answer: [2],
    explanation: '六淫致病的共同特点包括：外感性、季节性、地域性、相兼性、转化性。传染性是疠气致病的特点。',
    keyPoint: '六淫致病特点',
    difficulty: 2
  },
  {
    id: 6,
    type: 'single',
    subject: 'basic',
    question: '"阳虚则外寒"的理论依据是？',
    options: ['阳气不足，温煦功能减退', '阳气不足，气化功能减弱', '阳气不足，固摄功能失职', '阳气不足，推动功能减弱'],
    answer: [0],
    explanation: '阳气具有温煦形体的作用，阳气不足则温煦功能减退，故见畏寒肢冷等外寒表现。',
    keyPoint: '阳气的生理功能',
    difficulty: 2
  },
  {
    id: 7,
    type: 'single',
    subject: 'basic',
    question: '下列哪项属于"子盗母气"的传变？',
    options: ['心病及脾', '肝病及肾', '脾病及心', '肺病及肾'],
    answer: [2],
    explanation: '"子盗母气"是五行相生关系异常中的子病及母。脾为心之子（火生土），脾病及心即为子盗母气。',
    keyPoint: '五行相生关系异常',
    difficulty: 3
  },
  {
    id: 8,
    type: 'multiple',
    subject: 'basic',
    question: '下列哪些属于肾的生理功能？（多选）',
    options: ['主水', '纳气', '主骨生髓', '主运化', '主生长发育与生殖'],
    answer: [0, 1, 2, 4],
    explanation: '肾的生理功能包括：藏精，主生长发育与生殖；主水；主纳气。主运化是脾的功能。',
    keyPoint: '肾的生理功能',
    difficulty: 2
  },
  {
    id: 9,
    type: 'single',
    subject: 'basic',
    question: '中医认为"血"的生成主要与哪个脏腑关系最密切？',
    options: ['心', '肝', '脾', '肺'],
    answer: [2],
    explanation: '脾为后天之本，气血生化之源。脾胃运化的水谷精微是化生血液的最基本物质。',
    keyPoint: '血的生成',
    difficulty: 1
  },
  {
    id: 10,
    type: 'judge',
    subject: 'basic',
    question: '津和液在功能上没有区别，可以混称。',
    options: ['正确', '错误'],
    answer: [1],
    explanation: '津和液虽同属津液，但在性状、分布和功能上有所不同。津质地清稀，分布于体表皮肤、肌肉和孔窍，起滋润作用；液质地稠厚，分布于骨节、脏腑、脑髓，起濡养作用。',
    keyPoint: '津与液的区别',
    difficulty: 2
  },

  // ==================== 中医诊断学 ====================
  {
    id: 11,
    type: 'single',
    subject: 'diagnostics',
    question: '舌质淡白主？',
    options: ['虚寒证', '实热证', '阴虚证', '瘀血证'],
    answer: [0],
    explanation: '淡白舌主气血两虚、阳虚。气血不足或阳气虚弱，不能上荣于舌，故见舌质淡白。',
    keyPoint: '舌质主病',
    difficulty: 1
  },
  {
    id: 12,
    type: 'single',
    subject: 'diagnostics',
    question: '下列哪项不属于八纲辨证的内容？',
    options: ['表里', '寒热', '虚实', '阴阳'],
    answer: [3],
    explanation: '八纲辨证的内容是表里、寒热、虚实。阴阳是八纲的总纲，不与前三者并列，而是统领表里、寒热、虚实。',
    keyPoint: '八纲辨证',
    difficulty: 2
  },
  {
    id: 13,
    type: 'multiple',
    subject: 'diagnostics',
    question: '下列哪些属于望诊的内容？（多选）',
    options: ['望神色形态', '望舌象', '望分泌物', '望指纹', '听声音'],
    answer: [0, 1, 2, 3],
    explanation: '望诊包括望神色形态、望局部（舌象、分泌物、排泄物等）、望指纹。听声音属于闻诊。',
    keyPoint: '四诊——望诊',
    difficulty: 1
  },
  {
    id: 14,
    type: 'single',
    subject: 'diagnostics',
    question: '脉象"滑脉"主？',
    options: ['痰饮、食积、实热', '气滞血瘀', '阴虚', '阳虚水泛'],
    answer: [0],
    explanation: '滑脉往来流利，应指圆滑，如珠走盘。主痰饮、食积、实热。亦见于青壮年妇女的孕脉。',
    keyPoint: '常见脉象主病',
    difficulty: 2
  },
  {
    id: 15,
    type: 'judge',
    subject: 'diagnostics',
    question: '浮脉主表证，沉脉主里证，这是绝对的。',
    options: ['正确', '错误'],
    answer: [1],
    explanation: '浮脉一般主表证，但亦可见于虚阳外越之里证（如久病体虚脉浮大无力）；沉脉一般主里证，但亦可见于外感初起表邪束表之浮脉。',
    keyPoint: '脉诊的相对性',
    difficulty: 3
  },
  {
    id: 16,
    type: 'single',
    subject: 'diagnostics',
    question: '下列哪项是"肝气郁结"证的典型表现？',
    options: ['头痛眩晕', '胸胁胀痛、情志抑郁', '纳差便溏', '腰膝酸软'],
    answer: [1],
    explanation: '肝气郁结证以情志抑郁、胸胁或少腹胀痛为主要表现。肝失疏泄，气机郁滞，故见胸胁胀痛、善太息等。',
    keyPoint: '脏腑辨证——肝',
    difficulty: 2
  },
  {
    id: 17,
    type: 'single',
    subject: 'diagnostics',
    question: '黄苔主？',
    options: ['寒证', '热证', '湿证', '虚证'],
    answer: [1],
    explanation: '黄苔主热证、里证。热邪熏灼，故见黄苔。苔色越黄，热邪越重。淡黄为热轻，深黄为热重，焦黄为热极。',
    keyPoint: '苔色主病',
    difficulty: 1
  },
  {
    id: 18,
    type: 'multiple',
    subject: 'diagnostics',
    question: '下列哪些属于"阴虚证"的表现？（多选）',
    options: ['五心烦热', '盗汗', '口燥咽干', '形体肥胖', '舌红少苔'],
    answer: [0, 1, 2, 4],
    explanation: '阴虚证以形体消瘦、口燥咽干、五心烦热、潮热盗汗、舌红少苔、脉细数为常见表现。形体肥胖多见于痰湿内盛。',
    keyPoint: '阴虚证辨证要点',
    difficulty: 2
  },
  {
    id: 19,
    type: 'single',
    subject: 'diagnostics',
    question: '"问诊"中"十问歌"的首句是？',
    options: ['一问寒热二问汗', '一问头痛二问身', '一问饮食二问便', '一问起居二问因'],
    answer: [0],
    explanation: '明代张景岳《十问歌》："一问寒热二问汗，三问头身四问便，五问饮食六问胸，七聋八渴俱当辨，九问旧病十问因，再兼服药参机变。"',
    keyPoint: '问诊十问歌',
    difficulty: 1
  },
  {
    id: 20,
    type: 'single',
    subject: 'diagnostics',
    question: '面色青色主？',
    options: ['寒证、痛证、瘀血、惊风', '热证、湿证', '虚证、寒证', '脱证、失血'],
    answer: [0],
    explanation: '青色主寒证、痛证、瘀血、惊风。寒凝气滞，经脉瘀阻，或疼痛剧烈，或小儿惊风，均可见面色青。',
    keyPoint: '五色主病',
    difficulty: 2
  },

  // ==================== 中药学 ====================
  {
    id: 21,
    type: 'single',
    subject: 'herbology',
    question: '下列哪味药被称为"百草之王"？',
    options: ['黄芪', '人参', '白术', '甘草'],
    answer: [1],
    explanation: '人参能大补元气，为滋补强壮之要药，被誉为"百草之王"。',
    keyPoint: '补虚药——人参',
    difficulty: 1
  },
  {
    id: 22,
    type: 'single',
    subject: 'herbology',
    question: '下列哪味药具有"回阳救逆"的功效？',
    options: ['干姜', '肉桂', '附子', '吴茱萸'],
    answer: [2],
    explanation: '附子辛甘大热，能上助心阳以通脉，下补肾阳以益火，挽救散失之元阳，为"回阳救逆第一品药"。',
    keyPoint: '温里药——附子',
    difficulty: 2
  },
  {
    id: 23,
    type: 'multiple',
    subject: 'herbology',
    question: '下列哪些药物属于"十八反"中不宜同用的配伍？（多选）',
    options: ['人参配藜芦', '甘草配甘遂', '乌头配半夏', '丁香配郁金', '人参配五灵脂'],
    answer: [0, 1, 2],
    explanation: '十八反中：甘草反甘遂、大戟、芫花、海藻；乌头反半夏、瓜蒌、贝母、白蔹、白及；藜芦反人参、沙参、丹参、玄参、苦参、细辛、芍药。丁香配郁金属十九畏，人参配五灵脂亦属十九畏。',
    keyPoint: '中药配伍禁忌——十八反',
    difficulty: 3
  },
  {
    id: 24,
    type: 'single',
    subject: 'herbology',
    question: '下列哪味药是"四君子汤"的组成之一？',
    options: ['黄芪', '山药', '茯苓', '大枣'],
    answer: [2],
    explanation: '四君子汤由人参、白术、茯苓、甘草四味药组成，是益气健脾的基础方。',
    keyPoint: '补气方——四君子汤',
    difficulty: 1
  },
  {
    id: 25,
    type: 'judge',
    subject: 'herbology',
    question: '中药的"五味"是指辛、甘、酸、苦、咸五种实际味道。',
    options: ['正确', '错误'],
    answer: [1],
    explanation: '中药的五味不仅是药物味道的反映，更重要的是对药物作用的概括。辛能散能行，甘能补能缓能和，酸能收能涩，苦能泄能燥能坚，咸能软能下。此外还有淡味（能渗能利）和涩味。',
    keyPoint: '中药五味',
    difficulty: 2
  },
  {
    id: 26,
    type: 'single',
    subject: 'herbology',
    question: '下列哪味药具有"解表散寒、宣肺平喘、利水消肿"的功效？',
    options: ['桂枝', '麻黄', '紫苏', '防风'],
    answer: [1],
    explanation: '麻黄性温，味辛、微苦。功效：发汗解表，宣肺平喘，利水消肿。为发汗解表之第一药。',
    keyPoint: '解表药——麻黄',
    difficulty: 2
  },
  {
    id: 27,
    type: 'single',
    subject: 'herbology',
    question: '"十九畏"中，人参畏？',
    options: ['五灵脂', '朴硝', '郁金', '水银'],
    answer: [0],
    explanation: '十九畏中：人参畏五灵脂。此外还有丁香畏郁金，牙硝畏三棱等。',
    keyPoint: '中药配伍禁忌——十九畏',
    difficulty: 2
  },
  {
    id: 28,
    type: 'multiple',
    subject: 'herbology',
    question: '下列哪些属于清热解毒药？（多选）',
    options: ['金银花', '连翘', '石膏', '黄芩', '板蓝根'],
    answer: [0, 1, 4],
    explanation: '金银花、连翘、板蓝根属于清热解毒药。石膏属于清热泻火药，黄芩属于清热燥湿药。',
    keyPoint: '清热药的分类',
    difficulty: 2
  },
  {
    id: 29,
    type: 'single',
    subject: 'herbology',
    question: '下列哪味药被称为"气病之总司，女科之主帅"？',
    options: ['当归', '川芎', '白芍', '熟地'],
    answer: [1],
    explanation: '川芎活血行气，祛风止痛，为"血中之气药"，被称为"气病之总司，女科之主帅"。',
    keyPoint: '活血化瘀药——川芎',
    difficulty: 2
  },
  {
    id: 30,
    type: 'single',
    subject: 'herbology',
    question: '下列哪味药具有"安神"功效？',
    options: ['龙骨', '石决明', '牡蛎', '以上都是'],
    answer: [3],
    explanation: '龙骨、牡蛎均能重镇安神，石决明虽以平肝潜阳为主，但亦有安神之效。三味药均具有安神功效。',
    keyPoint: '安神药',
    difficulty: 1
  },

  // ==================== 方剂学 ====================
  {
    id: 31,
    type: 'single',
    subject: 'prescriptions',
    question: '"麻黄汤"的组成是？',
    options: ['麻黄、桂枝、杏仁、甘草', '麻黄、桂枝、白术、甘草', '麻黄、杏仁、石膏、甘草', '麻黄、桂枝、生姜、甘草'],
    answer: [0],
    explanation: '麻黄汤由麻黄、桂枝、杏仁、甘草组成。功能发汗解表，宣肺平喘。主治外感风寒表实证。',
    keyPoint: '解表剂——麻黄汤',
    difficulty: 1
  },
  {
    id: 32,
    type: 'single',
    subject: 'prescriptions',
    question: '下列哪首方剂被称为"群方之祖"？',
    options: ['四君子汤', '桂枝汤', '麻黄汤', '小柴胡汤'],
    answer: [1],
    explanation: '桂枝汤为《伤寒论》第一方，张仲景以桂枝汤为祖方，加减化裁出众多方剂，故被称为"群方之祖"。',
    keyPoint: '方剂学——桂枝汤',
    difficulty: 1
  },
  {
    id: 33,
    type: 'multiple',
    subject: 'prescriptions',
    question: '下列哪些方剂属于"四物汤"的加减方？（多选）',
    options: ['桃红四物汤', '胶艾四物汤', '四君子汤', '圣愈汤', '当归补血汤'],
    answer: [0, 1, 3],
    explanation: '桃红四物汤、胶艾四物汤、圣愈汤均由四物汤加减而来。四君子汤是独立的补气方，当归补血汤由黄芪、当归组成。',
    keyPoint: '补血剂——四物汤类方',
    difficulty: 2
  },
  {
    id: 34,
    type: 'single',
    subject: 'prescriptions',
    question: '"六味地黄丸"的组成是？',
    options: ['熟地、山萸肉、山药、泽泻、茯苓、丹皮', '熟地、当归、白芍、川芎', '人参、白术、茯苓、甘草', '黄芪、人参、白术、当归、甘草、陈皮'],
    answer: [0],
    explanation: '六味地黄丸由熟地黄、山茱萸、山药、泽泻、茯苓、牡丹皮组成。功能滋补肝肾，主治肝肾阴虚证。',
    keyPoint: '补阴剂——六味地黄丸',
    difficulty: 1
  },
  {
    id: 35,
    type: 'judge',
    subject: 'prescriptions',
    question: '"小柴胡汤"是和解少阳的代表方剂。',
    options: ['正确', '错误'],
    answer: [0],
    explanation: '小柴胡汤为和解少阳的代表方剂，由柴胡、黄芩、半夏、人参、甘草、生姜、大枣组成。主治伤寒少阳证。',
    keyPoint: '和解剂——小柴胡汤',
    difficulty: 1
  },
  {
    id: 36,
    type: 'single',
    subject: 'prescriptions',
    question: '下列哪首方剂主治"阳明腑实证"？',
    options: ['大承气汤', '小柴胡汤', '白虎汤', '麻黄汤'],
    answer: [0],
    explanation: '大承气汤由大黄、厚朴、枳实、芒硝组成。功能峻下热结，主治阳明腑实证（痞、满、燥、实）。',
    keyPoint: '泻下剂——大承气汤',
    difficulty: 2
  },
  {
    id: 37,
    type: 'single',
    subject: 'prescriptions',
    question: '"逍遥散"的功能是？',
    options: ['疏肝解郁，养血健脾', '清热泻火，疏肝解郁', '补益肝肾，疏肝解郁', '活血化瘀，疏肝解郁'],
    answer: [0],
    explanation: '逍遥散由柴胡、当归、白芍、白术、茯苓、甘草、薄荷、生姜组成。功能疏肝解郁，养血健脾。主治肝郁血虚脾弱证。',
    keyPoint: '理气剂——逍遥散',
    difficulty: 2
  },
  {
    id: 38,
    type: 'multiple',
    subject: 'prescriptions',
    question: '下列哪些方剂中含有"大黄"？（多选）',
    options: ['大承气汤', '大黄牡丹汤', '麻杏石甘汤', '茵陈蒿汤', '凉膈散'],
    answer: [0, 1, 3, 4],
    explanation: '大承气汤、大黄牡丹汤、茵陈蒿汤、凉膈散中均含有大黄。麻杏石甘汤由麻黄、杏仁、石膏、甘草组成，不含大黄。',
    keyPoint: '含大黄的方剂',
    difficulty: 2
  },
  {
    id: 39,
    type: 'single',
    subject: 'prescriptions',
    question: '"归脾汤"主治？',
    options: ['心脾气血两虚证', '脾胃气虚证', '脾不统血证', '心脾两虚与脾不统血证'],
    answer: [3],
    explanation: '归脾汤既能益气补血，健脾养心，又能益气摄血。主治心脾气血两虚证和脾不统血证。',
    keyPoint: '补益剂——归脾汤',
    difficulty: 2
  },
  {
    id: 40,
    type: 'single',
    subject: 'prescriptions',
    question: '下列哪首方剂体现了"培土生金"的治法？',
    options: ['参苓白术散', '麦门冬汤', '百合固金汤', '清燥救肺汤'],
    answer: [0],
    explanation: '参苓白术散以四君子汤益气健脾（培土），配伍山药、莲子等补肺气（生金），体现了培土生金的治法，主治脾虚湿盛证。',
    keyPoint: '补益剂——参苓白术散',
    difficulty: 3
  },

  // ==================== 针灸学 ====================
  {
    id: 41,
    type: 'single',
    subject: 'acupuncture',
    question: '下列哪条经脉属于"奇经八脉"？',
    options: ['足太阴脾经', '手少阳三焦经', '任脉', '足少阳胆经'],
    answer: [2],
    explanation: '奇经八脉包括：任脉、督脉、冲脉、带脉、阴跷脉、阳跷脉、阴维脉、阳维脉。',
    keyPoint: '奇经八脉',
    difficulty: 1
  },
  {
    id: 42,
    type: 'single',
    subject: 'acupuncture',
    question: '"足三里"穴属于哪条经脉？',
    options: ['足太阴脾经', '足阳明胃经', '足少阳胆经', '足厥阴肝经'],
    answer: [1],
    explanation: '足三里穴位于小腿前外侧，犊鼻穴下3寸，属于足阳明胃经。为保健要穴，有"肚腹三里留"之说。',
    keyPoint: '常用腧穴定位',
    difficulty: 1
  },
  {
    id: 43,
    type: 'multiple',
    subject: 'acupuncture',
    question: '下列哪些属于"五输穴"？（多选）',
    options: ['井穴', '荥穴', '输穴', '原穴', '合穴'],
    answer: [0, 1, 2, 4],
    explanation: '五输穴是指井、荥、输、经、合五个穴位的总称，分布在四肢肘膝关节以下。原穴不属于五输穴。',
    keyPoint: '特定穴——五输穴',
    difficulty: 2
  },
  {
    id: 44,
    type: 'single',
    subject: 'acupuncture',
    question: '下列哪项不是针灸的"得气"表现？',
    options: ['酸', '麻', '胀', '痛'],
    answer: [3],
    explanation: '得气（针感）主要表现为酸、麻、胀、重等感觉，沿经脉循行方向传导。如果出现剧烈疼痛，可能是针刺不当，不是得气。',
    keyPoint: '针刺得气',
    difficulty: 2
  },
  {
    id: 45,
    type: 'judge',
    subject: 'acupuncture',
    question: '"合谷"穴是孕妇禁用或慎用的穴位。',
    options: ['正确', '错误'],
    answer: [0],
    explanation: '合谷穴有催产、引产的作用，孕妇禁针或慎用，以免引起流产。',
    keyPoint: '针灸禁忌',
    difficulty: 1
  },
  {
    id: 46,
    type: 'single',
    subject: 'acupuncture',
    question: '下列哪项属于"八会穴"？',
    options: ['关元', '气海', '膻中', '中脘'],
    answer: [2],
    explanation: '八会穴是指脏、腑、气、血、筋、脉、骨、髓之气聚会的八个穴位。膻中为"气会"，关元为小肠募穴，气海为肓之原，中脘为胃募穴（腑会）。',
    keyPoint: '特定穴——八会穴',
    difficulty: 2
  },
  {
    id: 47,
    type: 'single',
    subject: 'acupuncture',
    question: '"督脉"被称为？',
    options: ['阴脉之海', '阳脉之海', '血海', '十二经脉之海'],
    answer: [1],
    explanation: '督脉行于背部正中，多次与手足三阳经及阳维脉交会，能总督一身之阳经，故称"阳脉之海"。',
    keyPoint: '督脉的功能',
    difficulty: 1
  },
  {
    id: 48,
    type: 'multiple',
    subject: 'acupuncture',
    question: '下列哪些属于"募穴"？（多选）',
    options: ['中脘（胃募）', '关元（小肠募）', '太渊（肺原）', '期门（肝募）', '京门（肾募）'],
    answer: [0, 1, 3, 4],
    explanation: '募穴是脏腑之气汇聚于胸腹部的穴位。中脘为胃募，关元为小肠募，期门为肝募，京门为肾募。太渊为肺经原穴，不是募穴。',
    keyPoint: '特定穴——募穴',
    difficulty: 2
  },
  {
    id: 49,
    type: 'single',
    subject: 'acupuncture',
    question: '手太阴肺经的起止穴位是？',
    options: ['中府至少商', '少商至少冲', '中府至少冲', '少商至少泽'],
    answer: [0],
    explanation: '手太阴肺经起于中府穴，止于少商穴。循行方向是从胸走手。',
    keyPoint: '手太阴肺经循行',
    difficulty: 2
  },
  {
    id: 50,
    type: 'single',
    subject: 'acupuncture',
    question: '下列哪种灸法属于"直接灸"？',
    options: ['隔姜灸', '隔蒜灸', '瘢痕灸', '温针灸'],
    answer: [2],
    explanation: '直接灸是将艾炷直接放在皮肤上施灸的方法，包括瘢痕灸（化脓灸）和无瘢痕灸。隔姜灸、隔蒜灸属于间接灸，温针灸属于艾卷灸。',
    keyPoint: '灸法分类',
    difficulty: 2
  }
]

/** 根据 ID 获取题目 */
export function getQuestionById(id: number): Question | undefined {
  return questions.find(q => q.id === id)
}

/** 根据科目获取题目 */
export function getQuestionsBySubject(subject: string): Question[] {
  if (subject === 'all') return questions
  return questions.filter(q => q.subject === subject)
}

/** 随机打乱数组 */
export function shuffleArray<T>(array: T[]): T[] {
  const arr = [...array]
  for (let i = arr.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [arr[i], arr[j]] = [arr[j], arr[i]]
  }
  return arr
}
