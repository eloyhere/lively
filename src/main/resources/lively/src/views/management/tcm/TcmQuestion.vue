<template>
  <Modulize :service="tcmQuestionService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <!-- 搜索条件 -->
    <template #search="{search}">
      <ElFormItem label="科目" prop="subject">
        <ElSelect v-model="search.subject" placeholder="全部科目" clearable style="width: 160px;">
          <ElOption v-for="opt in subjectOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem label="题型" prop="type">
        <ElSelect v-model="search.type" placeholder="全部题型" clearable style="width: 120px;">
          <ElOption label="单选题" value="single" />
          <ElOption label="多选题" value="multiple" />
          <ElOption label="判断题" value="judge" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem label="难度" prop="difficulty">
        <ElSelect v-model="search.difficulty" placeholder="全部难度" clearable style="width: 100px;">
          <ElOption label="基础" :value="1" />
          <ElOption label="中等" :value="2" />
          <ElOption label="困难" :value="3" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem label="题目" prop="question">
        <ElInput v-model="search.question" placeholder="搜索题目内容" clearable style="width: 200px;" />
      </ElFormItem>
      <ElFormItem>
        <ElButton type="success" :icon="Download" @click="exportCSV">导出</ElButton>
        <ElButton type="warning" :icon="Upload" @click="importCSV">导入</ElButton>
        <input ref="importFileInput" type="file" accept=".csv" style="display: none;" @change="handleImportFile" />
      </ElFormItem>
    </template>

    <!-- 表格列 -->
    <template #column>
      <ElTableColumn label="科目" prop="subject" width="120">
        <template #default="{row}">
          <ElTag :type="getSubjectTagType(row.subject)">{{ getSubjectLabel(row.subject) }}</ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn label="题型" prop="type" width="90">
        <template #default="{row}">
          <ElTag size="small">{{ getTypeLabel(row.type) }}</ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn label="题目内容" prop="question" min-width="300" show-overflow-tooltip />
      <ElTableColumn label="难度" prop="difficulty" width="80" align="center">
        <template #default="{row}">
          <ElRate v-model="row.difficulty" :max="3" disabled />
        </template>
      </ElTableColumn>
      <ElTableColumn label="考点" prop="keyPoint" width="150" show-overflow-tooltip />
      <ElTableColumn label="操作" width="80" fixed="right" align="center">
        <template #default="{row}">
          <ElButton type="primary" :icon="View" link @click="openPreview(row)">预览</ElButton>
        </template>
      </ElTableColumn>
    </template>

    <!-- 题目预览对话框 -->
    <ElDialog v-model="previewVisible" title="题目预览" width="680px">
      <div v-if="previewRow" style="padding: 10px;">
        <div style="margin-bottom: 16px;">
          <div style="font-weight: bold; font-size: 15px; margin-bottom: 8px;">题目内容</div>
          <div style="line-height: 1.8; white-space: pre-wrap;">{{ previewRow.question }}</div>
        </div>
        <div style="margin-bottom: 16px;" v-if="previewRow.type !== 'judge'">
          <div style="font-weight: bold; font-size: 15px; margin-bottom: 8px;">选项列表</div>
          <div v-for="(opt, idx) in parseOptions(previewRow.options)" :key="idx" style="margin-bottom: 6px; display: flex; align-items: center;">
            <span style="font-weight: bold; margin-right: 8px; min-width: 24px;">{{ String.fromCharCode(65 + idx) }}.</span>
            <ElTag v-if="isCorrectAnswer(previewRow, idx)" type="success" effect="dark" style="margin-right: 8px;">正确</ElTag>
            <span>{{ opt.value }}</span>
          </div>
        </div>
        <div style="margin-bottom: 16px;" v-else>
          <div style="font-weight: bold; font-size: 15px; margin-bottom: 8px;">选项列表</div>
          <div style="margin-bottom: 6px; display: flex; align-items: center;">
            <span style="font-weight: bold; margin-right: 8px;">A.</span>
            <ElTag v-if="isCorrectAnswer(previewRow, 0)" type="success" effect="dark" style="margin-right: 8px;">正确</ElTag>
            <span>正确</span>
          </div>
          <div style="display: flex; align-items: center;">
            <span style="font-weight: bold; margin-right: 8px;">B.</span>
            <ElTag v-if="isCorrectAnswer(previewRow, 1)" type="success" effect="dark" style="margin-right: 8px;">正确</ElTag>
            <span>错误</span>
          </div>
        </div>
        <div style="margin-bottom: 16px;">
          <div style="font-weight: bold; font-size: 15px; margin-bottom: 8px;">正确答案</div>
          <span>{{ formatAnswer(previewRow) }}</span>
        </div>
        <div style="margin-bottom: 16px;" v-if="previewRow.explanation">
          <div style="font-weight: bold; font-size: 15px; margin-bottom: 8px;">解析</div>
          <div style="line-height: 1.8; white-space: pre-wrap;">{{ previewRow.explanation }}</div>
        </div>
        <div style="margin-bottom: 16px;" v-if="previewRow.keyPoint">
          <div style="font-weight: bold; font-size: 15px; margin-bottom: 8px;">考点</div>
          <div>{{ previewRow.keyPoint }}</div>
        </div>
        <div>
          <div style="font-weight: bold; font-size: 15px; margin-bottom: 8px;">难度</div>
          <ElRate :model-value="previewRow.difficulty || 1" :max="3" disabled />
        </div>
      </div>
    </ElDialog>

    <!-- 新增表单 -->
    <template #insert="{insert}">
      <ElFormItem label="科目" prop="subject" required>
        <ElSelect v-model="insert.subject" placeholder="选择科目">
          <ElOption v-for="opt in subjectOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem label="题型" prop="type" required>
        <ElSelect v-model="insert.type" placeholder="选择题型" @change="handleTypeChange(insert)">
          <ElOption label="单选题" value="single" />
          <ElOption label="多选题" value="multiple" />
          <ElOption label="判断题" value="judge" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem label="题目" prop="question" required>
        <ElInput v-model="insert.question" type="textarea" :rows="3" placeholder="输入题目内容" />
      </ElFormItem>
      <ElFormItem label="选项" prop="options" required v-if="insert.type !== 'judge'">
        <div v-for="(opt, idx) in insertOptions" :key="idx" style="margin-bottom: 8px;">
          <ElInput v-model="opt.value" style="width: 400px;" :placeholder="'选项 ' + String.fromCharCode(65 + idx)">
            <template #prepend>{{ String.fromCharCode(65 + idx) }}</template>
          </ElInput>
          <ElButton v-if="idx > 1" type="danger" :icon="Delete" circle size="small" @click="removeOption(insertOptions, idx)" style="margin-left: 8px;" />
        </div>
        <ElButton type="primary" :icon="Plus" size="small" @click="addOption(insertOptions)">添加选项</ElButton>
      </ElFormItem>
      <ElFormItem label="正确答案" prop="answer" required>
        <template v-if="insert.type === 'judge'">
          <ElRadioGroup v-model="insert.answer">
            <ElRadio label="[0]">正确</ElRadio>
            <ElRadio label="[1]">错误</ElRadio>
          </ElRadioGroup>
        </template>
        <template v-else-if="insert.type === 'single'">
          <ElSelect v-model="insert.answer" placeholder="选择正确答案">
            <ElOption v-for="(opt, idx) in insertOptions" :key="idx" :label="String.fromCharCode(65 + idx) + '. ' + opt.value" :value="'[' + idx + ']'" />
          </ElSelect>
        </template>
        <template v-else>
          <ElSelect v-model="insert.answer" multiple placeholder="选择正确答案（多选）">
            <ElOption v-for="(opt, idx) in insertOptions" :key="idx" :label="String.fromCharCode(65 + idx) + '. ' + opt.value" :value="idx" />
          </ElSelect>
        </template>
      </ElFormItem>
      <ElFormItem label="解析" prop="explanation">
        <ElInput v-model="insert.explanation" type="textarea" :rows="3" placeholder="输入答案解析" />
      </ElFormItem>
      <ElFormItem label="考点" prop="keyPoint">
        <ElInput v-model="insert.keyPoint" placeholder="输入考点关键词" />
      </ElFormItem>
      <ElFormItem label="难度" prop="difficulty">
        <ElRate v-model="insert.difficulty" :max="3" show-score />
      </ElFormItem>
    </template>

    <!-- 修改表单 -->
    <template #update="{update}">
      <ElFormItem label="科目" prop="subject" required>
        <ElSelect v-model="update.subject" placeholder="选择科目">
          <ElOption v-for="opt in subjectOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem label="题型" prop="type" required>
        <ElSelect v-model="update.type" placeholder="选择题型" @change="handleTypeChange(update)">
          <ElOption label="单选题" value="single" />
          <ElOption label="多选题" value="multiple" />
          <ElOption label="判断题" value="judge" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem label="题目" prop="question" required>
        <ElInput v-model="update.question" type="textarea" :rows="3" placeholder="输入题目内容" />
      </ElFormItem>
      <ElFormItem label="选项" prop="options" required v-if="update.type !== 'judge'">
        <div v-for="(opt, idx) in updateOptions" :key="idx" style="margin-bottom: 8px;">
          <ElInput v-model="opt.value" style="width: 400px;" :placeholder="'选项 ' + String.fromCharCode(65 + idx)">
            <template #prepend>{{ String.fromCharCode(65 + idx) }}</template>
          </ElInput>
          <ElButton v-if="idx > 1" type="danger" :icon="Delete" circle size="small" @click="removeOption(updateOptions, idx)" style="margin-left: 8px;" />
        </div>
        <ElButton type="primary" :icon="Plus" size="small" @click="addOption(updateOptions)">添加选项</ElButton>
      </ElFormItem>
      <ElFormItem label="正确答案" prop="answer" required>
        <template v-if="update.type === 'judge'">
          <ElRadioGroup v-model="update.answer">
            <ElRadio label="[0]">正确</ElRadio>
            <ElRadio label="[1]">错误</ElRadio>
          </ElRadioGroup>
        </template>
        <template v-else-if="update.type === 'single'">
          <ElSelect v-model="update.answer" placeholder="选择正确答案">
            <ElOption v-for="(opt, idx) in updateOptions" :key="idx" :label="String.fromCharCode(65 + idx) + '. ' + opt.value" :value="'[' + idx + ']'" />
          </ElSelect>
        </template>
        <template v-else>
          <ElSelect v-model="update.answer" multiple placeholder="选择正确答案（多选）">
            <ElOption v-for="(opt, idx) in updateOptions" :key="idx" :label="String.fromCharCode(65 + idx) + '. ' + opt.value" :value="idx" />
          </ElSelect>
        </template>
      </ElFormItem>
      <ElFormItem label="解析" prop="explanation">
        <ElInput v-model="update.explanation" type="textarea" :rows="3" placeholder="输入答案解析" />
      </ElFormItem>
      <ElFormItem label="考点" prop="keyPoint">
        <ElInput v-model="update.keyPoint" placeholder="输入考点关键词" />
      </ElFormItem>
      <ElFormItem label="难度" prop="difficulty">
        <ElRate v-model="update.difficulty" :max="3" show-score />
      </ElFormItem>
    </template>
  </Modulize>
</template>

<script setup lang="ts">
import Modulize from "@/component/Modulize.vue";
import { TcmQuestionService } from "@/hooks/service.ts";
import { reactive, ref, type Reactive, watch, computed } from "vue";
import type { Query, TcmQuestion } from "@/declaration/entity.ts";
import type { Insert, Update } from "@/declaration/modulize.ts";
import { Delete, Plus, View, Download, Upload } from '@element-plus/icons-vue';
import { ElMessage } from "element-plus";

// Service
const tcmQuestionService: TcmQuestionService = new TcmQuestionService();

// 科目选项常量
const subjectOptions = [
  { label: "中医基础理论", value: "basic" },
  { label: "中医诊断学", value: "diagnostics" },
  { label: "中药学", value: "herbology" },
  { label: "方剂学", value: "prescriptions" },
  { label: "针灸学", value: "acupuncture" }
];

// 查询参数
const query: Reactive<Query<TcmQuestion>> = reactive<Query<TcmQuestion>>({
  page: 1,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {} as Partial<TcmQuestion>
});

// 数据列表
const data: Reactive<Array<TcmQuestion>> = reactive<Array<TcmQuestion>>([]);

// 新增状态
const insert: Reactive<Insert<TcmQuestion>> = reactive<Insert<TcmQuestion>>({
  show: false,
  target: {
    subject: "",
    type: "",
    question: "",
    options: "[]",
    answer: "[]",
    explanation: "",
    keyPoint: "",
    difficulty: 1
  } as Partial<TcmQuestion>
});

// 修改状态
const update: Reactive<Update<TcmQuestion>> = reactive<Update<TcmQuestion>>({
  show: false,
  target: {
    subject: "",
    type: "",
    question: "",
    options: "[]",
    answer: "[]",
    explanation: "",
    keyPoint: "",
    difficulty: 1
  } as Partial<TcmQuestion>
});

// 用于表单选项编辑的中间状态
const insertOptions = ref<Array<{ value: string }>>([{ value: "" }, { value: "" }, { value: "" }, { value: "" }]);
const updateOptions = ref<Array<{ value: string }>>([{ value: "" }, { value: "" }, { value: "" }, { value: "" }]);

// 同步 insert.options 到 insertOptions
watch(() => insert.target.options, (val) => {
  insertOptions.value = parseOptions(val);
}, { immediate: true });

// 同步 update.options 到 updateOptions
watch(() => update.target.options, (val) => {
  updateOptions.value = parseOptions(val);
}, { immediate: true });

// 同步 insertOptions 回 insert.options
watch(insertOptions, (val) => {
  insert.target.options = stringifyOptions(val);
}, { deep: true });

// 同步 updateOptions 回 update.options
watch(updateOptions, (val) => {
  update.target.options = stringifyOptions(val);
}, { deep: true });

// 辅助函数
function getSubjectLabel(subject: string): string {
  const map: Record<string, string> = {
    basic: "基础理论",
    diagnostics: "诊断学",
    herbology: "中药学",
    prescriptions: "方剂学",
    acupuncture: "针灸学"
  };
  return map[subject] || subject;
}

function getSubjectTagType(subject: string): string {
  const map: Record<string, string> = {
    basic: "primary",
    diagnostics: "danger",
    herbology: "success",
    prescriptions: "warning",
    acupuncture: "info"
  };
  return map[subject] || "";
}

function getTypeLabel(type: string): string {
  const map: Record<string, string> = {
    single: "单选",
    multiple: "多选",
    judge: "判断"
  };
  return map[type] || type;
}

function parseOptions(options: string | string[] | undefined): Array<{ value: string }> {
  // 如果已经是数组，直接使用
  if (Array.isArray(options)) {
    return options.map((v: string) => ({ value: v }));
  }
  // 空值处理
  if (!options || options === "[]" || options === "") {
    return [{ value: "" }, { value: "" }, { value: "" }, { value: "" }];
  }
  try {
    // 尝试标准 JSON 解析
    const arr = JSON.parse(options);
    if (Array.isArray(arr)) return arr.map((v: string) => ({ value: v }));
  } catch {
    // 兼容后端非标准格式: [整体观念, 辨证论治, ...]（无引号）
    if (options.startsWith('[') && options.endsWith(']')) {
      const content = options.slice(1, -1);
      const items = content.split(/,\s*/).map(s => s.trim()).filter(s => s);
      if (items.length > 0) return items.map(v => ({ value: v }));
    }
  }
  return [{ value: "" }, { value: "" }, { value: "" }, { value: "" }];
}

function stringifyOptions(opts: Array<{ value: string }>): string {
  return JSON.stringify(opts.map(o => o.value));
}

function handleTypeChange(target: Partial<TcmQuestion>) {
  if (target.type === "judge") {
    target.options = JSON.stringify(["正确", "错误"]);
    target.answer = "[0]";
  } else {
    target.options = "[]";
    target.answer = "[]";
  }
}

function addOption(opts: Array<{ value: string }>) {
  opts.push({ value: "" });
}

function removeOption(opts: Array<{ value: string }>, idx: number) {
  if (opts.length > 2) {
    opts.splice(idx, 1);
  } else {
    ElMessage.warning("至少需要保留 2 个选项");
  }
}

// 监听多选题答案变化，转换为 JSON 字符串
watch(() => update.target.answer, (val) => {
  if (update.target.type === "multiple" && Array.isArray(val)) {
    update.target.answer = JSON.stringify(val);
  }
});

watch(() => insert.target.answer, (val) => {
  if (insert.target.type === "multiple" && Array.isArray(val)) {
    insert.target.answer = JSON.stringify(val);
  }
});

// ========== 题目预览 ==========
const previewVisible = ref(false);
const previewRow = ref<Partial<TcmQuestion> | null>(null);

function openPreview(row: Partial<TcmQuestion>) {
  previewRow.value = { ...row };
  previewVisible.value = true;
}

function isCorrectAnswer(row: Partial<TcmQuestion>, idx: number): boolean {
  if (!row.answer) return false;
  try {
    // 处理 answer 可能是数组或字符串的情况
    const arr = Array.isArray(row.answer) ? row.answer : JSON.parse(row.answer);
    return arr.includes(idx);
  } catch {
    return false;
  }
}

function formatAnswer(row: Partial<TcmQuestion>): string {
  if (!row.answer) return "";
  try {
    // 处理 answer 可能是数组或字符串的情况
    const arr = Array.isArray(row.answer) ? row.answer : JSON.parse(row.answer);
    if (row.type === "judge") {
      return arr.includes(0) ? "正确" : "错误";
    }
    return arr.map((i: number) => String.fromCharCode(65 + i)).join("、");
  } catch {
    return String(row.answer);
  }
}

// ========== 批量导入导出 ==========
const importFileInput = ref<HTMLInputElement | null>(null);

function escapeCSVField(field: string): string {
  if (field.includes(",") || field.includes('"') || field.includes("\n")) {
    return '"' + field.replace(/"/g, '""') + '"';
  }
  return field;
}

async function exportCSV() {
  try {
    // 获取所有数据（不分页）
    const allQuery: Query<TcmQuestion> = {
      page: 0,
      total: 0,
      size: 99999,
      direction: "ASC",
      target: { ...query.target }
    };
    const res = await tcmQuestionService.findAllPagedBy(allQuery);
    const list = res?.content || [];

    // 表头
    const headers = ["科目", "题型", "题目", "选项A", "选项B", "选项C", "选项D", "选项E", "选项F", "正确答案", "解析", "考点", "难度"];
    const rows: string[] = [headers.join(",")];

    for (const item of list) {
      const opts = parseOptions(item.options);
      let answerStr = "";
      try {
        const arr = JSON.parse(item.answer || "[]");
        answerStr = arr.join(",");
      } catch {
        answerStr = item.answer || "";
      }

      const row = [
        escapeCSVField(item.subject || ""),
        escapeCSVField(item.type || ""),
        escapeCSVField(item.question || ""),
        escapeCSVField(opts[0]?.value || ""),
        escapeCSVField(opts[1]?.value || ""),
        escapeCSVField(opts[2]?.value || ""),
        escapeCSVField(opts[3]?.value || ""),
        escapeCSVField(opts[4]?.value || ""),
        escapeCSVField(opts[5]?.value || ""),
        escapeCSVField(answerStr),
        escapeCSVField(item.explanation || ""),
        escapeCSVField(item.keyPoint || ""),
        escapeCSVField(String(item.difficulty || 1))
      ];
      rows.push(row.join(","));
    }

    const csvContent = "\uFEFF" + rows.join("\n");
    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = "题目导出_" + new Date().toISOString().slice(0, 10) + ".csv";
    link.click();
    URL.revokeObjectURL(url);
    ElMessage.success("导出成功");
  } catch (e) {
    console.error("导出失败", e);
    ElMessage.error("导出失败");
  }
}

function importCSV() {
  importFileInput.value?.click();
}

function parseCSVLine(line: string): string[] {
  const result: string[] = [];
  let current = "";
  let inQuotes = false;
  for (let i = 0; i < line.length; i++) {
    const ch = line[i];
    if (inQuotes) {
      if (ch === '"') {
        if (i + 1 < line.length && line[i + 1] === '"') {
          current += '"';
          i++;
        } else {
          inQuotes = false;
        }
      } else {
        current += ch;
      }
    } else {
      if (ch === '"') {
        inQuotes = true;
      } else if (ch === ",") {
        result.push(current);
        current = "";
      } else {
        current += ch;
      }
    }
  }
  result.push(current);
  return result;
}

async function handleImportFile(event: Event) {
  const input = event.target as HTMLInputElement;
  const file = input.files?.[0];
  if (!file) return;

  try {
    const text = await file.text();
    const lines = text.split("\n").map(l => l.trim()).filter(l => l.length > 0);
    if (lines.length < 2) {
      ElMessage.warning("文件内容为空");
      return;
    }

    // 跳过表头
    let successCount = 0;
    let failCount = 0;

    for (let i = 1; i < lines.length; i++) {
      try {
        const cols = parseCSVLine(lines[i]);
        if (cols.length < 13) {
          failCount++;
          continue;
        }

        const subject = cols[0];
        const type = cols[1];
        const question = cols[2];
        const optionsArr: string[] = [];
        for (let j = 3; j <= 8; j++) {
          if (cols[j]) optionsArr.push(cols[j]);
        }
        const answerIndices = cols[9].split(",").map(s => parseInt(s.trim())).filter(n => !isNaN(n));
        const explanation = cols[10];
        const keyPoint = cols[11];
        const difficulty = parseInt(cols[12]) || 1;

        const newItem: Partial<TcmQuestion> = {
          subject,
          type,
          question,
          options: JSON.stringify(optionsArr),
          answer: JSON.stringify(answerIndices),
          explanation,
          keyPoint,
          difficulty
        };

        await tcmQuestionService.insert(newItem);
        successCount++;
      } catch {
        failCount++;
      }
    }

    ElMessage.success(`导入完成：成功 ${successCount} 条，失败 ${failCount} 条`);
    // 刷新列表
    const refreshQuery: Query<TcmQuestion> = {
      page: 1,
      total: 0,
      size: query.size,
      direction: query.direction,
      target: {} as Partial<TcmQuestion>
    };
    tcmQuestionService.findAllPagedBy(refreshQuery).then(res => {
      query.total = res?.page?.totalElements || 0;
      data.length = 0;
      (res?.content || []).forEach(entity => data.push(entity as TcmQuestion));
    });
  } catch (e) {
    console.error("导入失败", e);
    ElMessage.error("导入失败");
  } finally {
    // 重置 input 以便重复选择同一文件
    input.value = "";
  }
}
</script>

<style scoped>
</style>
