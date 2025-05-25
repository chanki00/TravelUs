<template>
  <div
    v-if="show"
    class="fixed inset-0 z-50 bg-black bg-opacity-30 flex items-center justify-center"
  >
    <div class="bg-white rounded-lg shadow-lg w-full max-w-md p-6">
      <h2 class="text-lg font-semibold mb-4">초대할 여행 계획 선택</h2>

      <div v-if="plans.length === 0" class="text-gray-500 text-center">여행 계획이 없습니다.</div>

      <ul v-else class="space-y-2 max-h-60 overflow-y-auto">
        <li
          v-for="plan in plans"
          :key="plan.id"
          class="p-3 border rounded cursor-pointer hover:bg-gray-50 flex justify-between items-center"
          @click="select(plan)"
        >
          <span>{{ plan.title }}</span>
          <span class="text-xs text-gray-500">{{ plan.duration }}일</span>
        </li>
      </ul>

      <button class="mt-6 text-sm text-gray-500 hover:underline" @click="$emit('close')">
        닫기
      </button>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  show: Boolean,
  plans: {
    type: Array,
    default: () => [],
  },
  inviteeId: {
    type: Number,
    required: true,
  },
})

const emit = defineEmits(['select', 'close'])

function select(plan) {
  emit('select', {
    plan,
    inviteeId: props.inviteeId,
  })
}
</script>

<style scoped></style>
